package com.ggboy.exam.service.impl;

import com.ggboy.exam.beans.exam.StuInfo;
import com.ggboy.exam.beans.exam.StuTeaCourseLink;
import com.ggboy.exam.beans.exam.TeaAccess;
import com.ggboy.exam.beans.exam.TeaCourseLink;
import com.ggboy.exam.beans.itemBank.Subject;
import com.ggboy.exam.common.ResultEnum;
import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.dao.exam.StuTeaCourseLinkDao;
import com.ggboy.exam.dao.exam.TeaAccessDao;
import com.ggboy.exam.dao.exam.TeaCourseLinkDao;
import com.ggboy.exam.dao.itemBank.UserDao;
import com.ggboy.exam.service.TeaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    @Override
    public ResultResponse getTeaCourse(String userId) {

        List<Subject> subjects = teaCourseLinkDao.selectTeaSub(Integer.parseInt(userId));
        if (subjects.size() == 0){
            return ResultResponse.fail("当前用户没有课程");
        }
        return ResultResponse.success(subjects);
    }

    @Override
    public ResultResponse addCourse(Integer userId, List<Integer> courseIds) {
        List<Integer> courseIds1 = teaCourseLinkDao.selectTeaSubIds(userId);
        for (Integer courseId :
                courseIds) {
            for (Integer courseId1 :
                    courseIds1) {
                if (courseId.equals(courseId1)){
                    return ResultResponse.fail("请勿允许选择已拥有的课程");
                }
            }
        }
        courseIds.forEach(courseId ->
                teaCourseLinkDao.insert(new TeaCourseLink(userId, courseId)));

        return ResultResponse.success();
    }

    @Override
    public ResultResponse searchCourseStu(String courseId,String userId) {
        List<StuInfo> stuInfos = stuTeaCourseLinkDao
                .searchStuByCourseId(Integer.parseInt(courseId), Integer.parseInt(userId));
        if (stuInfos.size() == 0){
            return ResultResponse.fail("当前课程暂无学生信息");
        }
        return ResultResponse.success(stuInfos);
    }

    @Override
    public ResultResponse deleteCourseStu(String stuId, Integer courseId) {
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

        if (!access){
            teaAccessDao.refuseApply(accessId);
            return ResultResponse.success();
        }
        teaAccessDao.accessApply(accessId);
        TeaAccess teaAccess = teaAccessDao.selectOne(new TeaAccess(accessId));
        StuTeaCourseLink stuTeaCourseLink = new StuTeaCourseLink(teaAccess.getStuId(),
                teaAccess.getCourseId(),
                teaAccess.getTeaId());
        stuTeaCourseLinkDao.insert(stuTeaCourseLink);
        return ResultResponse.success();

    }

}
