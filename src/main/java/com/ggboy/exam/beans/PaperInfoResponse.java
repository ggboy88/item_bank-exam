package com.ggboy.exam.beans;

import com.ggboy.exam.beans.itemBank.BigQst;
import com.ggboy.exam.beans.itemBank.ChoiceQst;
import com.ggboy.exam.beans.itemBank.DesignQst;
import com.ggboy.exam.beans.itemBank.TOFQst;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author qiang
 * @Description //TODO 试卷详情信息
 * @Date 10:20 2020/11/18
 * @Param
 * @return
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaperInfoResponse {

    private Integer paperId;

    private String paperCourse;

    private String paperMadeDate;

    private String paperTeacher;

    private List<ChoiceQst> paperChoiceInfo;

    private List<TOFQst> paperTOFInfo;

    private List<DesignQst> paperDesignInfo;

    private List<BigQst> paperBigInfo;

    private String paperLevel;

}
