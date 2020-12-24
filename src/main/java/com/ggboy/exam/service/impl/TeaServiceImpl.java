package com.ggboy.exam.service.impl;

import com.ggboy.exam.beans.CourseStuResponse;
import com.ggboy.exam.beans.exam.*;
import com.ggboy.exam.beans.itemBank.Subject;
import com.ggboy.exam.beans.itemBank.User;
import com.ggboy.exam.common.ResultEnum;
import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.dao.exam.StuDao;
import com.ggboy.exam.dao.exam.StuTeaCourseLinkDao;
import com.ggboy.exam.dao.exam.TeaAccessDao;
import com.ggboy.exam.dao.exam.TeaCourseLinkDao;
import com.ggboy.exam.dao.itemBank.CourseDao;
import com.ggboy.exam.dao.itemBank.UserDao;
import com.ggboy.exam.service.TeaService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeaServiceImpl implements TeaService {

    @Resource
    private UserDao userDao;

    @Resource
    private TeaCourseLinkDao teaCourseLinkDao;

    @Resource
    private StuTeaCourseLinkDao stuTeaCourseLinkDao;

    @Resource
    private TeaAccessDao teaAccessDao;

    @Resource
    private StuDao stuDao;

    @Resource
    private CourseDao courseDao;

    @Override
    public ResultResponse getTeaCourse(String userId,Integer pageSize,Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        List<Subject> subjects = teaCourseLinkDao.selectTeaSub(Integer.parseInt(userId));
        if (subjects.size() == 0){
            return ResultResponse.fail("当前用户没有课程");
        }
        PageInfo<Subject> pageInfo = new PageInfo<>(subjects);
        return ResultResponse.success(pageInfo);
    }

    @Override
    public ResultResponse addCourse(Integer userId, List<String> courseIds) {
        courseIds.forEach(courseId -> {
            Example example = Example.builder(TeaCourseLink.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo("teaId", userId)
                            .andEqualTo("courseId", courseId))
                    .build();
            TeaCourseLink teaCourseLink = teaCourseLinkDao.selectOneByExample(example);
            if (teaCourseLink != null && teaCourseLink.getStatus().equals("0")){
                teaCourseLink.setStatus("1");
                teaCourseLinkDao.updateByExampleSelective(teaCourseLink,example);
            }else {
                teaCourseLinkDao.insert(new TeaCourseLink(userId, courseId));
            }
        }
        );

        return ResultResponse.success();
    }

    @Override
    public ResultResponse searchCourseStu(String userId) {
        List<Subject> subjects = teaCourseLinkDao.selectTeaSub(Integer.parseInt(userId));
        if (subjects.size() == 0){
            return ResultResponse.fail("当前用户没有课程");
        }
        List<CourseStuResponse> list = new ArrayList<>();
        subjects.forEach(subject -> {
            CourseStuResponse courseStuResponse = new CourseStuResponse();
            List<StuInfo> stuInfos = stuTeaCourseLinkDao
                    .searchStuByCourseId(Integer.parseInt(subject.getCourseId()), Integer.parseInt(userId));
            if (stuInfos != null){
                // 屏蔽掉学生密码和盐
                delShowStuInfo(stuInfos);
                courseStuResponse.setStuInfos(stuInfos);
            }
            courseStuResponse.setSubject(subject);
            list.add(courseStuResponse);
        });

        //屏蔽密码和盐

        if (list.size() == 0){
            return ResultResponse.fail("当前教师暂无学生信息");
        }
        return ResultResponse.success(list);
    }

    @Override
    public ResultResponse deleteCourseStu(String stuId, String courseId) {
        StuTeaCourseLink stuTeaCourseLink = new StuTeaCourseLink();
        stuTeaCourseLink.setId(null);
        stuTeaCourseLink.setStuId(stuId);
        stuTeaCourseLink.setCourseId(courseId);
        try {
            stuTeaCourseLinkDao.delete(stuTeaCourseLink);
        }catch (Exception e){
            e.printStackTrace();
            return ResultResponse.fail(ResultEnum.SYSTEM_ERROR);
        }
        return ResultResponse.success();
    }

    @Transactional
    @Override
    public ResultResponse accessStuApply(Boolean access, String accessId) {
        TeaAccess teaAccess = teaAccessDao.selectByPrimaryKey(accessId);
        if (!access){
            teaAccess.setAccess(false);
            teaAccessDao.updateByPrimaryKey(teaAccess);
            return ResultResponse.success();
        }
        teaAccess.setAccess(true);
        teaAccessDao.updateByPrimaryKey(teaAccess);
        StuTeaCourseLink stuTeaCourseLink = new StuTeaCourseLink(teaAccess.getStuId(),
                teaAccess.getCourseId(),
                teaAccess.getTeaId());
        stuTeaCourseLinkDao.insert(stuTeaCourseLink);
        return ResultResponse.success();

    }

    @Override
    public ResultResponse selectStuApply(Integer userId,Integer pageNum,Integer pageSize) {
        Example example = Example.builder(TeaAccess.class)
                .andWhere(Sqls.custom()
                        .andEqualTo("teaId", userId)
                        .andIsNull("access")).build();
        PageHelper.startPage(pageNum,pageSize);
        List<TeaAccess> teaAccesses = teaAccessDao.selectByExample(example);
        List<StuApplyResponse> stuApplyResponses = new ArrayList<>();
        teaAccesses.forEach(teaAccess -> {
            StuInfo stuInfo = stuDao.selectByPrimaryKey(teaAccess.getStuId());
            Subject subject = courseDao.selectByPrimaryKey(teaAccess.getCourseId());
            StuApplyResponse stuApplyResponse = new StuApplyResponse();
            stuApplyResponse.setTeaAccess(teaAccess);
            stuApplyResponse.setStuName(stuInfo.getUsername());
            stuApplyResponse.setCourseName(subject.getCourseName());
            stuApplyResponses.add(stuApplyResponse);
        });

        if (teaAccesses.size() == 0){
            return ResultResponse.fail("未查询到申请信息");
        }
        return ResultResponse.success(stuApplyResponses);
    }

    @Override
    public ResultResponse selectUser(Integer userId) {
        User user = userDao.selectByPrimaryKey(userId);
        return ResultResponse.success(user);
    }

    @Override
    public ResultResponse selectStuApplyAccount(Integer userId) {
        Example example = Example.builder(TeaAccess.class)
                .andWhere(Sqls.custom().andEqualTo("teaId", userId).andIsNull("access")).build();
        int count = teaAccessDao.selectCountByExample(example);
        return ResultResponse.success(count);
    }

    @Override
    public ResultResponse deleteTeaCourse(String userId, String courseId) {
        Example example = Example.builder(TeaCourseLink.class).andWhere(Sqls.custom()
                .andEqualTo("teaId", Integer.parseInt(userId))
                .andEqualTo("courseId", courseId)).build();
        TeaCourseLink teaCourseLink = new TeaCourseLink();
        teaCourseLink.setId(null);
        teaCourseLink.setStatus("0");
        try{
            teaCourseLinkDao.updateByExampleSelective(teaCourseLink,example);
        }catch (Exception e){
            e.printStackTrace();
            return ResultResponse.fail(ResultEnum.SYSTEM_ERROR);
        }
        return ResultResponse.success();
    }

    private void delShowStuInfo(List<StuInfo> stuInfos){
        stuInfos.forEach(stuInfo -> {
            stuInfo.setPassword(null);
            stuInfo.setSalt(null);
        });
    }

}
