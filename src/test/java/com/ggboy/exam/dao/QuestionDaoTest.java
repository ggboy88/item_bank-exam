package com.ggboy.exam.dao;

import com.ggboy.exam.beans.itemBank.ChoiceQst;
import com.ggboy.exam.dao.itemBank.ChoiceDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionDaoTest {

    @Resource
    private ChoiceDao choiceDao;

    @Test
    public void testChoiceDao(){
        ChoiceQst choiceQst = choiceDao.selectChoiceQstByQuestionId("20200504214640");
        System.out.println(choiceQst);
    }

}
