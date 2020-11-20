package com.ggboy.exam.dao;

import com.ggboy.exam.beans.itemBank.Paper;
import com.ggboy.exam.dao.itemBank.PaperDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PaperDaoTest {

    @Resource
    private PaperDao paperDao;

    @Test
    public void testSelectPaperByUserIdAndCourse(){

        List<Paper> papers = paperDao.selectPaperByCourseAndUserId("1001", 1);
        papers.forEach(System.out::println);
    }

}
