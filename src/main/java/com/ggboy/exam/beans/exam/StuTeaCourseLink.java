package com.ggboy.exam.beans.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.UUID;

@Data
@NoArgsConstructor
@Table(name = "stu_tea_course_link")
public class StuTeaCourseLink {

    @Column(name = "id")
    private String id = UUID.randomUUID().toString();

    @Column(name = "stu_id")
    private String stuId;

    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "tea_id")
    private Integer teaId;

    public StuTeaCourseLink(String stuId, Integer courseId, Integer teaId) {
        this.stuId = stuId;
        this.courseId = courseId;
        this.teaId = teaId;
    }
}
