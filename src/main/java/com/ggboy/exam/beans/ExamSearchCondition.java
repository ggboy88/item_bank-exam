package com.ggboy.exam.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamSearchCondition {

    private Integer pageNum = 1;

    private Integer pageSize = 5;

    private Timestamp startTime;

    private Timestamp endTime;

    private String courseId;

    private String paperId;

    private String status;

}
