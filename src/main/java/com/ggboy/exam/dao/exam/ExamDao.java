package com.ggboy.exam.dao.exam;

import com.ggboy.exam.beans.ExamSearchCondition;
import com.ggboy.exam.beans.StuCourseInfoResponse;
import com.ggboy.exam.beans.exam.ExamInfo;
import com.ggboy.exam.config.GeneralMapper;

import java.util.List;

public interface ExamDao extends GeneralMapper<ExamInfo> {
    List<StuCourseInfoResponse> selectAllCourse(ExamSearchCondition examSearchCondition);
}
