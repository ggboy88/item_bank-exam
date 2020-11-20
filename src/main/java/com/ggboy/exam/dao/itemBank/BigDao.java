package com.ggboy.exam.dao.itemBank;

import com.ggboy.exam.beans.itemBank.BigQst;
import com.ggboy.exam.config.GeneralMapper;
import org.apache.ibatis.annotations.Param;

public interface BigDao extends GeneralMapper<BigQst> {
    BigQst selectBigQstByQuestionId(@Param("bigQuestion") String bigQuestion);
}
