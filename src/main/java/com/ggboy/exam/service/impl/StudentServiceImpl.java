package com.ggboy.exam.service.impl;

import com.ggboy.exam.beans.itemBank.Subject;
import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.dao.exam.StuSpecialtyLinkDao;
import com.ggboy.exam.service.StudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Resource
    private StuSpecialtyLinkDao stuSpecialtyLinkDao;

    @Override
    public ResultResponse selectCourse(String stuId) {

        List<Subject> subjects = stuSpecialtyLinkDao.selectSubjectsByStuId(stuId);
        if (subjects.size() == 0){
            return ResultResponse.fail("当前学院课程为0，请联系老师处理");
        }
        return ResultResponse.success(subjects);
    }
}
