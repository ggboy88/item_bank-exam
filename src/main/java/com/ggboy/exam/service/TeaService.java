package com.ggboy.exam.service;

import com.ggboy.exam.common.ResultResponse;

import java.util.List;

public interface TeaService {

    ResultResponse getTeaCourse(String userId,Integer pageSize,Integer pageNum);

    ResultResponse addCourse(Integer userId, List<String> courseIds);

    ResultResponse searchCourseStu(String userId);

    ResultResponse deleteCourseStu(String stuId,String courseId);

    ResultResponse accessStuApply(Boolean access,String accessId);

    ResultResponse selectStuApply(Integer userId,Integer pageNum,Integer pageSize);

    ResultResponse selectUser(Integer userId);

    ResultResponse selectStuApplyAccount(Integer userId);

    ResultResponse deleteTeaCourse(String userId,String courseId);

}
