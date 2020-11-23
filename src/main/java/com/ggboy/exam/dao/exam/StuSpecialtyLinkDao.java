package com.ggboy.exam.dao.exam;

import com.ggboy.exam.beans.exam.StuSpecialtyLink;
import com.ggboy.exam.beans.itemBank.Subject;
import com.ggboy.exam.config.GeneralMapper;

import java.util.List;

public interface StuSpecialtyLinkDao extends GeneralMapper<StuSpecialtyLink> {

    List<Subject> selectSubjectsByStuId(String stuId);

}
