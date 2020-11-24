package com.ggboy.exam.service;

import com.ggboy.exam.beans.TeaCourseLinkResponse;
import com.ggboy.exam.common.ResultResponse;

public interface StudentService {

    ResultResponse searchCourseAndTeacher(String stuId,Integer pageNum,Integer pageSize);

    ResultResponse sendApply(String userId, String courseId,Integer teaId);

    ResultResponse selectCourse(String userId);

    ResultResponse startExam(String examId);
}
