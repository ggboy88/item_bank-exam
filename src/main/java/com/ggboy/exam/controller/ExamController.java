package com.ggboy.exam.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ggboy.exam.beans.ExamSearchCondition;
import com.ggboy.exam.beans.exam.ExamInfo;
import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.service.ExamService;
import com.ggboy.exam.utils.TokenUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author qiang
 * @Description //TODO 考试相关功能接口
 * @Date 11:11 2020/11/20
 */
@RestController
@RequestMapping("/exam")
public class ExamController {

    @Resource
    private ExamService examService;
    
    /**
     * @Author qiang
     * @Description //TODO 添加考试（教师）
     * @Date 14:25 2020/11/17
     * @Param [examInfo]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @PostMapping("/addExam")
    public ResultResponse addExam(@RequestBody JSONObject examInfo,HttpServletRequest request){
        String token = request.getHeader("token");
        String user = TokenUtil.getUserId(token);
        return examService.addExam(examInfo,Integer.parseInt(user));
    }
    
    /**
     * @Author qiang
     * @Description //TODO 查询试卷详情信息（教师）
     * @Date 14:26 2020/11/17
     * @Param [paperId]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/paper/details")
    public ResultResponse searchPaper(@RequestParam("paperId") Integer paperId){
        return examService.searchPaper(paperId);
    }

    /**
     * @Author qiang
     * @Description //TODO 查询当前用户选择科目下的所有试卷信息（教师）
     * @Date 10:04 2020/11/18
     * @Param [courseId]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/list/paper")
    public ResultResponse listPapers(@RequestParam("courseId") String courseId,
                                     @RequestParam(name = "pageNum",defaultValue = "1") Integer pageNum,
                                     @RequestParam(name = "pageSize",defaultValue = "5") Integer pageSize,
                                     HttpServletRequest request){
        String token = request.getHeader("token");
        String user = TokenUtil.getUserId(token);
        return examService.listPaper(courseId,Integer.parseInt(user),pageNum,pageSize);
    }

    /**
     * @Author qiang
     * @Description //TODO 查询考试信息（教师）
     * @Date 14:25 2020/11/18
     * @Param [pageNum, pageSize, startTime, EndTime, courseId, paperId, status]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/list/exam")
    public ResultResponse listExam(@RequestParam(name = "pageNum",defaultValue = "1",required = false) Integer pageNum,
                                   @RequestParam(name = "pageSize",defaultValue = "5",required = false) Integer pageSize,
                                   @RequestParam(value = "startTime",required = false) Timestamp startTime,
                                   @RequestParam(value = "endTime",required = false) Timestamp endTime,
                                   @RequestParam(value = "courseName",required = false) String courseName,
                                   @RequestParam(value = "paperId",required = false) String paperId,
                                   @RequestParam(value = "status",required = false) String status,
                                   HttpServletRequest request){
        ExamSearchCondition examSearchCondition = new ExamSearchCondition(pageNum, pageSize, startTime, endTime, courseName, paperId, status);
        String token = request.getHeader("token");
        String user = TokenUtil.getUserId(token);
        return examService.listExam(examSearchCondition,Integer.parseInt(user));
    }

    /**
     * @Author qiang
     * @Description //TODO 考试开始前提醒考生
     * @Date 16:04 2021/1/11
     * @Param [examId]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/alarmExam")
    public ResultResponse alarmExam(@RequestParam("examId") String examId){
        String[] split = examId.split("alarm");
        return examService.alarmExam(split[0]);
    }

    /**
     * @Author qiang
     * @Description //TODO 删除考试信息
     * @Date 16:15 2021/1/11
     * @Param [examId]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/deleteExam")
    public ResultResponse deleteExam(@RequestParam("examId") String examId){
        String[] split = examId.split("delete");
        return examService.deleteExam(split[0]);
    }

    /**
     * @Author ggboy88
     * @Description //TODO 查看是否有正在进行的考试
     * @Date 2021/1/18 16:49
     * @Param [request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/hasExam")
    public ResultResponse hasExam(HttpServletRequest request){
        String token = request.getHeader("token");
        String user = TokenUtil.getUserId(token);
        return examService.hasExam(user);
    }

    @PostMapping("/submit")
    public ResultResponse submitExam(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        String token = request.getHeader("token");
        String user = TokenUtil.getUserId(token);
        Map<String,Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return examService.submitExamAnsw(map,user);
    }

    @GetMapping("/isSubmit")
    public ResultResponse isSubmit(HttpServletRequest request){
        String token = request.getHeader("token");
        String user = TokenUtil.getUserId(token);
        return examService.isSubmit(user);
    }

    /**
     * @Author ggboy88
     * @Description //TODO 查询考试结束时间
     * @Date 2021/1/21 11:07
     * @Param [request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/examTime")
    public ResultResponse examTime(HttpServletRequest request){
        String token = request.getHeader("token");
        String user = TokenUtil.getUserId(token);
        return examService.examTime(user);
    }

}
