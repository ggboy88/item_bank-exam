package com.ggboy.exam.dao.exam;

import com.ggboy.exam.beans.exam.TeaCourseLink;
import com.ggboy.exam.beans.itemBank.Subject;
import com.ggboy.exam.config.GeneralMapper;

import java.util.List;

public interface TeaCourseLinkDao extends GeneralMapper<TeaCourseLink> {

    List<Subject> selectTeaSub(Integer userId);

    List<String> selectTeaSubIds(Integer userId);

}
