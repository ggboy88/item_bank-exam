package com.ggboy.exam.beans.exam;

import com.ggboy.exam.annotation.UUIDGenId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tea_course_link")
public class TeaCourseLink {

    @Id
    @KeySql(genId = UUIDGenId.class)
    @Column(name = "id")
    private String id;

    @Column(name = "tea_id")
    private Integer teaId;

    @Column(name = "course_id")
    private String courseId;

    @Column(name = "status")
    private String status = "1";

    public TeaCourseLink(Integer teaId, String courseId) {
        this.teaId = teaId;
        this.courseId = courseId;
    }
}
