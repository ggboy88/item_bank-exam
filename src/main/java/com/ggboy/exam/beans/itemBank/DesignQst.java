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
@Table(name = "designtestinfo")
public class DesignQst {
    @Id
    @Column(name = "Design_id")
    private Integer designId;
    @Column(name = "Design_question")
    private String designQuestion;
    @Column(name = "Design_info")
    private String designInfo;
    @Column(name = "Design_answ")
    private String designAnsw;

}
