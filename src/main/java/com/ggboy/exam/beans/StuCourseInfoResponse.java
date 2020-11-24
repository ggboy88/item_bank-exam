package com.ggboy.exam.beans;

import com.ggboy.exam.beans.exam.ExamInfo;
import com.ggboy.exam.beans.itemBank.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StuCourseInfoResponse {

    private Subject subject;

    private String teaName;

    private Boolean hasExam = false;

    List<ExamInfo> examInfos;

}
