package com.ggboy.exam.beans.exam;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ggboy.exam.common.ExamEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Data
@Table(name = "exam_info")
@AllArgsConstructor
@NoArgsConstructor
public class ExamInfo {

    @Id
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();

    @Column(name = "course_id")
    private String courseId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "end_time")
    private Date endTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "paper_id")
    private Integer paperId;

    @Column(name = "status")
    private String status = ExamEnum.PENDING.getEncode();

    @Column(name = "teacher_id")
    private Integer teacherId;

}
