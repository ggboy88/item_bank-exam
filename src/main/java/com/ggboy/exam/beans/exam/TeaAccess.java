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
@Table(name = "tea_access")
public class TeaAccess {

    @Id
    @KeySql(genId = UUIDGenId.class)
    @Column(name = "id")
    private String id;

    @Column(name = "course_id")
    private String courseId;

    @Column(name = "stu_id")
    private String stuId;

    @Column(name = "tea_id")
    private Integer teaId;

    @Column(name = "access")
    private Boolean access;

    public TeaAccess(String id) {
        this.id = id;
    }

    public TeaAccess(String courseId, String stuId, Integer teaId) {
        this.courseId = courseId;
        this.stuId = stuId;
        this.teaId = teaId;
    }
}

