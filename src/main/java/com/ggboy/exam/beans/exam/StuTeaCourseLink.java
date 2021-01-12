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
@Table(name = "stu_tea_course_link")
public class StuTeaCourseLink {

    @Id
    @KeySql(genId = UUIDGenId.class)
    @Column(name = "id")
    private String id;

    @Column(name = "stu_id")
    private String stuId;

    @Column(name = "course_id")
    private String courseId;

    @Column(name = "tea_id")
    private Integer teaId;

    @Column(name = "delete_flag")
    private Integer deleteFlag = 1;

    public StuTeaCourseLink(String stuId, String courseId, Integer teaId) {
        this.stuId = stuId;
        this.courseId = courseId;
        this.teaId = teaId;
    }
}
