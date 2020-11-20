package com.ggboy.exam.redisTest;

import com.ggboy.exam.ExamApplication;
import com.ggboy.exam.utils.RedisUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest(classes = ExamApplication.class)
@RunWith(SpringRunner.class)
public class RedisTest {

    @Resource
    private RedisUtils redisUtils;

    @Test
    public void get(){
        String zhangsan = String.valueOf(redisUtils.get("zhangsan"));
        System.out.println(zhangsan);
    }

    @Test
    public void set(){
        redisUtils.set("zhangsan","123");
    }

}
