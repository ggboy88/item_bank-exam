package com.ggboy.exam.service.impl;

import com.ggboy.exam.beans.itemBank.Specialty;
import com.ggboy.exam.beans.itemBank.Subject;
import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.dao.itemBank.CourseDao;
import com.ggboy.exam.dao.itemBank.SpecialtyDao;
import com.ggboy.exam.service.CourseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Resource
    private CourseDao courseDao;

    @Resource
    private SpecialtyDao specialtyDao;

    @Override
    public ResultResponse selectCourseList(String userId) {
        List<Subject> subjects = courseDao.selectSubByTeaId(Integer.parseInt(userId));
        if (subjects.size() != 0){
            return ResultResponse.success(subjects);
        }
        return ResultResponse.success("当前用户可选课程为0");
    }

    @Override
    public ResultResponse selectSpecialty() {
        List<Specialty> specialties = specialtyDao.selectAll();
        return ResultResponse.success(specialties);
    }

}
