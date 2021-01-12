package com.ggboy.exam.service.impl;

import com.ggboy.exam.beans.CourseStuResponse;
import com.ggboy.exam.beans.exam.*;
import com.ggboy.exam.beans.itemBank.Subject;
import com.ggboy.exam.beans.itemBank.User;
import com.ggboy.exam.beans.vo.UpdateUserVo;
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
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
    public ResultResponse searchCourseStu(String courseId,String userId) {
        List<StuInfo> stuInfos = stuTeaCourseLinkDao
                .searchStuByCourseId(Integer.parseInt(courseId), Integer.parseInt(userId));
        //屏蔽密码和盐
        delShowStuInfo(stuInfos);
        if (stuInfos.size() == 0){
            return ResultResponse.fail("当前课程暂无学生信息");
        }
        return ResultResponse.success(stuInfos);
    }

    @Override
    public ResultResponse clearAllStu(String courseId, String userId) {

        Example example = Example.builder(StuTeaCourseLink.class).andWhere(Sqls.custom()
                .andEqualTo("courseId", courseId)
                .andEqualTo("teaId", Integer.parseInt(userId))).build();
        StuTeaCourseLink link = new StuTeaCourseLink();
        link.setId(null);
        link.setDeleteFlag(0);
        try {
            stuTeaCourseLinkDao.updateByExampleSelective(link,example);
        }catch (Exception e){
            e.printStackTrace();
            return ResultResponse.fail(ResultEnum.SYSTEM_ERROR);
        }
        return ResultResponse.success();
    }

    @Override
    public ResultResponse deleteCourseStu(String stuId, String courseId,String user) {
        Example example = Example.builder(StuTeaCourseLink.class)
                .andWhere(Sqls.custom()
                        .andEqualTo("stuId", stuId)
                        .andEqualTo("courseId", courseId)
                        .andEqualTo("teaId", Integer.parseInt(user))).build();
        StuTeaCourseLink stuTeaCourseLink = new StuTeaCourseLink();
        stuTeaCourseLink.setId(null);
        stuTeaCourseLink.setDeleteFlag(0);
        try {
            stuTeaCourseLinkDao.updateByExampleSelective(stuTeaCourseLink,example);
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
        if (teaAccesses.size() == 0){
            return ResultResponse.fail("未查询到申请信息");
        }
        List<StuApplyResponse> stuApplyResponses = new ArrayList<>();
        teaAccesses.forEach(teaAccess -> {
            StuInfo stuInfo = stuDao.selectByPrimaryKey(teaAccess.getStuId());
            Subject subject = courseDao.selectByPrimaryKey(teaAccess.getCourseId());
            StuApplyResponse stuApplyResponse = new StuApplyResponse();
            stuApplyResponse.setStuName(stuInfo.getUsername());
            stuApplyResponse.setStuPhone(stuInfo.getPhone());
            stuApplyResponse.setCourseName(subject.getCourseName());
            stuApplyResponse.setAccessId(teaAccess.getId());
            stuApplyResponses.add(stuApplyResponse);
        });
        PageInfo<StuApplyResponse> pageInfo = new PageInfo<>(stuApplyResponses);

        return ResultResponse.success(pageInfo);
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

    @Override
    public ResultResponse getOwnMsg(String userId) {
        User user = userDao.selectByPrimaryKey(Integer.parseInt(userId));
        return ResultResponse.success(user);
    }

    @Override
    public ResultResponse updateUser(UpdateUserVo updateUserVo, String userId) {
        User user = new User();
        user.setId(Integer.parseInt(userId));
        user.setUsername(updateUserVo.getUsername());
        user.setPassword(updateUserVo.getPassword());
        user.setTeacherName(updateUserVo.getName());

        userDao.updateByPrimaryKeySelective(user);

        return ResultResponse.success();
    }

    @Override
    public ResultResponse uploadHead(MultipartFile file,String userId){
        String path = "D:/我的文档/Documents/HBuilderProjects/exam/static/img/head/"+userId;
        String originalFilename = file.getOriginalFilename();

        assert originalFilename != null;
        String[] split = originalFilename.split("\\.");
        String fileName = System.currentTimeMillis() + userId +"."+split[1];
        byte[] bytes;
        try {
            bytes = file.getBytes();
            uploadPic(bytes,path,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultResponse.fail(e.getMessage());
        }
        User user = new User();
        user.setId(Integer.parseInt(userId));
        user.setImageUrl(path+"/"+fileName);
        userDao.updateByPrimaryKeySelective(user);

        return ResultResponse.success();
    }

    /**
     * @Author qiang
     * @Description //TODO 图片上传
     * @Date 14:47 2020/12/30
     * @Param [file, filePath, fileName]
     * @return void
     */
    private void uploadPic(byte[] file,String filePath,String fileName) throws IOException {
        File targetFile = new File(filePath);
        if (!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(filePath+"/"+fileName);
        fileOutputStream.write(file);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    private void delShowStuInfo(List<StuInfo> stuInfos){
        stuInfos.forEach(stuInfo -> {
            stuInfo.setPassword(null);
            stuInfo.setSalt(null);
        });
    }

}
