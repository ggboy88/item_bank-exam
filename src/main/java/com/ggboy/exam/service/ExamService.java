package com.ggboy.exam.service;

import com.ggboy.exam.beans.ExamSearchCondition;
import com.ggboy.exam.beans.exam.ExamInfo;
import com.ggboy.exam.common.ResultResponse;

import java.util.Date;

public interface ExamService {

    ResultResponse addExam(ExamInfo examInfo);

    ResultResponse searchPaper(Integer paperId);

    ResultResponse listPaper(String courseId,Integer userId);

    ResultResponse listExam(ExamSearchCondition examSearchCondition,Integer userId);

    ResultResponse startExam(Date time);

    ResultResponse endExam(Date time);
}
