package com.ggboy.exam.service;

import com.ggboy.exam.beans.vo.UpdateUserVo;
import com.ggboy.exam.common.ResultResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TeaService {

    ResultResponse getTeaCourse(String userId,Integer pageSize,Integer pageNum);

    ResultResponse addCourse(Integer userId, List<String> courseIds);

    ResultResponse searchCourseStu(String courseId,String userId);

    ResultResponse clearAllStu(String courseId,String userId);

    ResultResponse deleteCourseStu(String stuId,String courseId,String user);

    ResultResponse accessStuApply(Boolean access,String accessId);

    ResultResponse selectStuApply(Integer userId,Integer pageNum,Integer pageSize);

    ResultResponse selectUser(Integer userId);

    ResultResponse selectStuApplyAccount(Integer userId);

    ResultResponse deleteTeaCourse(String userId,String courseId);

    ResultResponse getOwnMsg(String userId);

    ResultResponse updateUser(UpdateUserVo updateUserVo,String userId);

    ResultResponse uploadHead(MultipartFile file,String userId);

}
