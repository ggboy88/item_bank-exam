package com.ggboy.exam.beans;

import com.ggboy.exam.beans.exam.StuInfo;
import com.ggboy.exam.beans.itemBank.Subject;
import lombok.Data;

import java.util.List;

@Data
public class CourseStuResponse {

    private Subject subject;

    private List<StuInfo> stuInfos;

}
