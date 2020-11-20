package com.ggboy.exam.dao.itemBank;

import com.ggboy.exam.beans.itemBank.DesignQst;
import com.ggboy.exam.config.GeneralMapper;
import org.apache.ibatis.annotations.Param;

public interface DesignDao extends GeneralMapper<DesignQst> {
    DesignQst selectDesignQstByQuestionId(@Param("designQuestion") String designQuestion);
}
