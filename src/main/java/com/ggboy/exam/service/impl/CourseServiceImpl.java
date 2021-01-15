package com.ggboy.exam.service.impl;

import com.ggboy.exam.beans.exam.StuSpecialtyLink;
import com.ggboy.exam.beans.exam.TeaCourseLink;
import com.ggboy.exam.beans.itemBank.Specialty;
import com.ggboy.exam.beans.itemBank.Subject;
import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.dao.exam.StuSpecialtyLinkDao;
import com.ggboy.exam.dao.exam.TeaCourseLinkDao;
import com.ggboy.exam.dao.itemBank.CourseDao;
import com.ggboy.exam.dao.itemBank.SpecialtyDao;
import com.ggboy.exam.service.CourseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Resource
    private CourseDao courseDao;

    @Resource
    private SpecialtyDao specialtyDao;

    @Resource
    private TeaCourseLinkDao teaCourseLinkDao;

    @Resource
    private StuSpecialtyLinkDao stuSpecialtyLinkDao;

    @Override
    public ResultResponse selectCourseList(String userId,Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Subject> subjects = courseDao.selectSubByTeaId(Integer.parseInt(userId));
        PageInfo<Subject> pageInfo = new PageInfo<>(subjects);
        if (subjects.size() != 0){
            return ResultResponse.success(pageInfo);
        }
        return ResultResponse.fail("当前用户可选课程为0");
    }

    @Override
    public ResultResponse selectSpecialty() {
        List<Specialty> specialties = specialtyDao.selectAll();
        return ResultResponse.success(specialties);
    }

    @Override
    public ResultResponse selectTeaSpecialtyName(String userId) {
        String specialtyName = specialtyDao.selectTeaSpecialtyName(Integer.parseInt(userId));
        return ResultResponse.success(specialtyName);
    }

    @Override
    public ResultResponse selectStuSpecialtyName(String userId) {
        Example example = Example.builder(StuSpecialtyLink.class)
                .andWhere(Sqls.custom().andEqualTo("stuId", userId)).build();
        StuSpecialtyLink stuSpecialtyLink = stuSpecialtyLinkDao.selectOneByExample(example);
        Specialty specialty = specialtyDao.selectByPrimaryKey(stuSpecialtyLink.getSpecialtyId());
        return ResultResponse.success(specialty.getSpecialtyName());
    }

}
