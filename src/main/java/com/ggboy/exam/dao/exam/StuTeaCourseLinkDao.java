package com.ggboy.exam.dao.exam;


import com.ggboy.exam.beans.exam.StuInfo;
import com.ggboy.exam.beans.exam.StuTeaCourseLink;
import com.ggboy.exam.config.GeneralMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StuTeaCourseLinkDao extends GeneralMapper<StuTeaCourseLink> {

    List<StuInfo> searchStuByCourseId(@Param("courseId") Integer courseId,@Param("teaId") Integer teaId);

}
