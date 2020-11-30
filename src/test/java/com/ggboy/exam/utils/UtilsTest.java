package com.ggboy.exam.utils;

import com.ggboy.exam.beans.exam.ExamInfo;
import com.ggboy.exam.common.ExamEnum;
import com.ggboy.exam.dao.exam.ExamDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UtilsTest {

    @Resource
    private ExamDao examDao;

    @Test
    public void testGetNowDate() throws ParseException {
        Date nowDate = DateUtil.getNowDate();
        System.out.println(nowDate);
    }

    @Test
    public void test(){
        Example example = Example.builder(ExamInfo.class).select()
                .andWhere(Sqls.custom()
                        .andEqualTo("status", ExamEnum.PROCESSING.getEncode())
                        .andLessThanOrEqualTo("endTime",new Date(System.currentTimeMillis())))
                .build();
        List<ExamInfo> examInfos = examDao.selectByExample(example);
        examInfos.forEach(System.out::println);
    }

    @Test
    public void MD5Utils(){
        String str = MD5Util.MD5("1lizhiqiangzz");
        System.out.println(str);
    }

    @Test
    public void test1(){
        String s = "/中国电信股份有限公司四川分公司/省公司本部/管理维护";
        String[] split = s.split("/");
        System.out.println(split[3]);
    }

}
