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
@Table(name = "bigqstioninfo")
public class BigQst {
    @Id
    @Column(name = "Big_id")
    private Integer BigId;
    @Column(name = "Big_question")
    private String BigQuestion;
    @Column(name = "Big_info")
    private String BigInfo;
    @Column(name = "Big_answ")
    private String BigAnsw;

}
