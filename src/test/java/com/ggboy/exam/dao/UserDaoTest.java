package com.ggboy.exam.dao;

import com.ggboy.exam.ExamApplication;
import com.ggboy.exam.beans.itemBank.User;
import com.ggboy.exam.dao.itemBank.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExamApplication.class)
public class UserDaoTest {

    @Resource
    private UserDao userDao;

    @Test
    public void userDaoTest(){
        User user = userDao.selectOne(new User("zhangsan", "1234"));
        System.out.println(user);
    }

}
