package com.ggboy.exam.beans.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StuRegisterParam{

    private Integer teaId;

    private Integer courseId;

    private StuInfo stuInfo;

}
