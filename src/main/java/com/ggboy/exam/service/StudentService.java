package com.ggboy.exam.service;

import com.ggboy.exam.beans.TeaCourseLinkResponse;
import com.ggboy.exam.beans.vo.UpdateUserVo;
import com.ggboy.exam.common.ResultResponse;
import org.springframework.web.multipart.MultipartFile;

public interface StudentService {

    ResultResponse searchCourseAndTeacher(String stuId,Integer pageNum,Integer pageSize);

    ResultResponse sendApply(String userId, String courseId,Integer teaId);

    ResultResponse selectCourse(String userId);

    ResultResponse startExam(String examId);

    ResultResponse queryStu(String userId);

    ResultResponse checkPass(String password,String userId);

    ResultResponse updateUser(UpdateUserVo updateUserVo, String userId);

    ResultResponse uploadHead(MultipartFile file, String userId);

    ResultResponse getAlarm(String userId);
}
