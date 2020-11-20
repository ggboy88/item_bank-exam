package com.ggboy.exam.beans.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tea_course_link")
public class TeaCourseLink {

    @Column(name = "id")
    private String id = UUID.randomUUID().toString();

    @Column(name = "tea_id")
    private Integer teaId;

    @Column(name = "course_id")
    private Integer courseId;

    public TeaCourseLink(Integer teaId, Integer courseId) {
        this.teaId = teaId;
        this.courseId = courseId;
    }
}
