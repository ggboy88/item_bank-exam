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
@Table(name = "tea_access")
public class TeaAccess {

    @Column(name = "id")
    private String id = UUID.randomUUID().toString();

    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "stu_id")
    private String stuId;

    @Column(name = "tea_id")
    private Integer teaId;

    @Column(name = "access")
    private Boolean access;

    public TeaAccess(String id) {
        this.id = id;
    }
}

