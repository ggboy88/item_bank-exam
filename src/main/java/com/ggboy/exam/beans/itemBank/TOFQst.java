package com.ggboy.exam.beans.itemBank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tofinfo")
public class TOFQst {
    @Id
    @Column(name = "TOrF_id")
    private Integer TOFId;
    @Column(name = "TOF_question")
    private String TOFQuestion;
    @Column(name = "TOF_info")
    private String TOFInfo;
    @Column(name = "TOF_answ")
    private String TOFAnsw;

}
