package com.ggboy.exam.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author qiang
 * @Description //TODO 试卷列表页面展示数据
 * @Date 10:50 2020/11/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaperListResponse {

    private Integer paperId;

    private String paperMadeDate;

    private String paperTeacher;

    private String paperLevel;

    private String paperCourse;

}
