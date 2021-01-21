package com.ggboy.exam.beans.exam;

import com.ggboy.exam.annotation.UUIDGenId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author ggboy88
 * @Description //TODO
 * @Date 2021/1/21 11:28
 */
@Data
@Table(name = "stu_exam_answ")
@AllArgsConstructor
@NoArgsConstructor
public class StuExamLink {

    @Id
    @KeySql(genId = UUIDGenId.class)
    @Column(name = "id")
    private String id;

    @Column(name = "stu_id")
    private String stuId;

    @Column(name = "paper_id")
    private Integer paperId;

    @Column(name = "question_id")
    private String questionId;

    @Column(name = "answ")
    private String answ;

    @Column(name = "exam_id")
    private String examId;


}
