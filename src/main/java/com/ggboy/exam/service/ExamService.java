package com.ggboy.exam.service;

import com.alibaba.fastjson.JSONObject;
import com.ggboy.exam.beans.ExamSearchCondition;
import com.ggboy.exam.beans.exam.ExamInfo;
import com.ggboy.exam.common.ResultResponse;

import java.util.Date;

public interface ExamService {

    ResultResponse addExam(JSONObject examInfo,Integer userId);

    ResultResponse searchPaper(Integer paperId);

    ResultResponse listPaper(String courseId,Integer userId,Integer pageNum,Integer pageSize);

    ResultResponse listExam(ExamSearchCondition examSearchCondition,Integer userId);

    ResultResponse startExam(Date time);

    ResultResponse endExam(Date time);

    ResultResponse alarmExam(String examId);

    ResultResponse deleteExam(String examId);
}
