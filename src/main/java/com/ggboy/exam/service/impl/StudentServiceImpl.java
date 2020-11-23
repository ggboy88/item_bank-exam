package com.ggboy.exam.service.impl;

import com.ggboy.exam.beans.TeaCourseLinkResponse;
import com.ggboy.exam.beans.exam.TeaAccess;
import com.ggboy.exam.beans.exam.TeaCourseLink;
import com.ggboy.exam.beans.itemBank.Subject;
import com.ggboy.exam.beans.itemBank.User;
import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.dao.exam.StuSpecialtyLinkDao;
import com.ggboy.exam.dao.exam.TeaAccessDao;
import com.ggboy.exam.dao.exam.TeaCourseLinkDao;
import com.ggboy.exam.dao.itemBank.UserDao;
import com.ggboy.exam.service.StudentService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Resource
    private StuSpecialtyLinkDao stuSpecialtyLinkDao;

    @Resource
    private TeaCourseLinkDao teaCourseLinkDao;

    @Resource
    private UserDao userDao;

    @Resource
    private TeaAccessDao teaAccessDao;

    @Override
    public ResultResponse searchCourseAndTeacher(String stuId,Integer pageNum,Integer pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        List<Subject> subjects = stuSpecialtyLinkDao.selectSubjectsByStuId(stuId);
        if (subjects.size() == 0){
            return ResultResponse.fail("当前学院课程为0，请联系老师处理");
        }
        List<TeaCourseLinkResponse> teaCourseLinkResponses = new ArrayList<>();
        subjects.forEach(subject -> {
            List<User> users = selectTeacher(subject.getCourseId());
            TeaCourseLinkResponse teaCourseLinkResponse = new TeaCourseLinkResponse();
            teaCourseLinkResponse.setUsers(users);
            teaCourseLinkResponse.setSubject(subject);
            teaCourseLinkResponses.add(teaCourseLinkResponse);
        });
        return ResultResponse.success(teaCourseLinkResponses);
    }

    @Override
    public ResultResponse sendApply(String userId, String courseId, Integer teaId) {
        TeaAccess teaAccess = new TeaAccess(courseId,userId,teaId);
        teaAccessDao.insert(teaAccess);
        return ResultResponse.success("请求发送成功，等待教师审核");
    }

    @Override
    public ResultResponse selectCourse(String userId) {
        return null;
    }

    private List<User> selectTeacher(String courseId) {
        Example example = Example.builder(TeaCourseLink.class)
                .andWhere(Sqls.custom().andEqualTo("courseId", courseId)).build();
        List<TeaCourseLink> teaCourseLinks = teaCourseLinkDao.selectByExample(example);
        if (teaCourseLinks.size() == 0){
            return null;
        }
        List<User> users = new ArrayList<>();
        teaCourseLinks.forEach(teaCourseLink -> {
            Example example1 = Example.builder(User.class)
                    .andWhere(Sqls.custom().andEqualTo("id", teaCourseLink.getTeaId())).build();
            User user = userDao.selectOneByExample(example1);
            users.add(user);
        });
        return users;
    }
}
