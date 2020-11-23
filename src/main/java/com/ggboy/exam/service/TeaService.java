package com.ggboy.exam.service;

import com.ggboy.exam.common.ResultResponse;

import java.util.List;

public interface TeaService {

    ResultResponse getTeaCourse(String userId);

    ResultResponse addCourse(Integer userId, List<Integer> courseIds);

    ResultResponse searchCourseStu(String courseId,String userId);

    ResultResponse deleteCourseStu(String stuId,String courseId);

    ResultResponse accessStuApply(Boolean access,String accessId);

    ResultResponse selectStuApply(Integer userId,Integer pageNum,Integer pageSize);

}
