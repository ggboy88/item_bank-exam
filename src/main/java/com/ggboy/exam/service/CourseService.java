package com.ggboy.exam.service;

import com.ggboy.exam.common.ResultResponse;

public interface CourseService {

    ResultResponse selectCourseList(String userId);

    ResultResponse selectSpecialty();

}
