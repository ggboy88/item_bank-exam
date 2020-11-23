package com.ggboy.exam.controller;

import com.ggboy.exam.beans.ExamSearchCondition;
import com.ggboy.exam.beans.exam.ExamInfo;
import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.service.ExamService;
import com.ggboy.exam.utils.TokenUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
    public ResultResponse addExam(@RequestBody ExamInfo examInfo){
        return examService.addExam(examInfo);
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
    public ResultResponse listPapers(@RequestParam("courseId") String courseId, HttpServletRequest request){
        String token = request.getHeader("token");
        String user = TokenUtil.getUserId(token);
        return examService.listPaper(courseId,Integer.parseInt(user));
    }

    /**
     * @Author qiang
     * @Description //TODO 查询考试信息（教师）
     * @Date 14:25 2020/11/18
     * @Param [pageNum, pageSize, startTime, EndTime, courseId, paperId, status]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @PostMapping("/list/exam")
    public ResultResponse listExam(@RequestBody ExamSearchCondition examSearchCondition,HttpServletRequest request){
        String token = request.getHeader("token");
        String user = TokenUtil.getUserId(token);
        return examService.listExam(examSearchCondition,Integer.parseInt(user));
    }



}
