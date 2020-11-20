package com.ggboy.exam.dao.itemBank;

import com.ggboy.exam.beans.itemBank.TOFQst;
import com.ggboy.exam.config.GeneralMapper;
import org.apache.ibatis.annotations.Param;

public interface TOFDao extends GeneralMapper<TOFQst> {

    TOFQst selectTOFQstByQuestionId(@Param("tofQuestion") String tofQuestion);

}
