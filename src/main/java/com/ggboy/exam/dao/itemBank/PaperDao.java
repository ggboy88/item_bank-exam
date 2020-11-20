package com.ggboy.exam.dao.itemBank;

import com.ggboy.exam.beans.itemBank.Paper;
import com.ggboy.exam.config.GeneralMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaperDao extends GeneralMapper<Paper> {

    List<Paper> selectPaperByCourseAndUserId(@Param("courseId") String courseId,@Param("userId") Integer userId);

}
