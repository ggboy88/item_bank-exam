package com.ggboy.exam.dao.itemBank;

import com.ggboy.exam.beans.itemBank.Subject;
import com.ggboy.exam.config.GeneralMapper;

import java.util.List;

public interface CourseDao extends GeneralMapper<Subject> {

    List<Subject> selectSubByTeaId(Integer teaId);

    String selectNameById(String courseId);
}
