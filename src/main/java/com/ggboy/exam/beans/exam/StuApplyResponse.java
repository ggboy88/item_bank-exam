package com.ggboy.exam.beans.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StuApplyResponse {

    private String courseName;

    private String stuName;

    private String access;

    private TeaAccess teaAccess;

}
