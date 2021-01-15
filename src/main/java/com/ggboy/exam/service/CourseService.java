package com.ggboy.exam.service;

import com.ggboy.exam.common.ResultResponse;

public interface CourseService {

    ResultResponse selectCourseList(String userId,Integer pageNum,Integer pageSize);

    ResultResponse selectSpecialty();

    ResultResponse selectTeaSpecialtyName(String userId);

    ResultResponse selectStuSpecialtyName(String userId);
}
