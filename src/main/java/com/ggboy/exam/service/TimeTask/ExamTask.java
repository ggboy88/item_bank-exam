package com.ggboy.exam.service.TimeTask;

import com.ggboy.exam.service.ExamService;
import com.ggboy.exam.utils.DateUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ExamTask {

    @Resource
    private ExamService examService;

    @Async("examExecutor")
    @Scheduled(cron = "0 0/5 * * * ?")
    public void startExam() throws ParseException {
        examService.startExam(DateUtil.getNowDate());
    }

    @Async("examExecutor")
    @Scheduled(cron = "0 0/5 * * * ?")
    public void endExam()throws ParseException {
        examService.endExam(DateUtil.getNowDate());
    }

}
