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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stu_specialty_link")
public class StuSpecialtyLink {

    @Id
    @KeySql(genId = UUIDGenId.class)
    @Column(name = "id")
    private String id;

    @Column(name = "stu_id")
    private String stuId;

    @Column(name = "specialty_id")
    private Integer specialtyId;

    public StuSpecialtyLink(String stuId, Integer specialtyId) {
        this.stuId = stuId;
        this.specialtyId = specialtyId;
    }
}
