package com.ggboy.exam.controller;

import com.ggboy.exam.beans.TeaCourseLinkResponse;
import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.service.StudentService;
import com.ggboy.exam.utils.TokenUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author qiang
 * @Description //TODO 学生相关功能接口
 * @Date 11:09 2020/11/20
 */
@RestController
@RequestMapping("/stu")
public class StuController {

    @Resource
    private StudentService studentService;

    /**
     * @Author qiang
     * @Description //TODO 查看当前学生可选课程信息,以及教师信息
     * @Date 11:22 2020/11/20
     * @Param []
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/searchCourseAndTeacher")
    public ResultResponse searchCourseAndTeacher(HttpServletRequest request
            ,@RequestParam(name = "pageNum",defaultValue = "1") Integer pageNum
            ,@RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize){
        String token = request.getHeader("token");
        String user = TokenUtil.getUserId(token);
        return studentService.searchCourseAndTeacher(user,pageNum,pageSize);
    }

    /**
     * @Author qiang
     * @Description //TODO 申请加入课程
     * @Date 16:50 2020/11/23
     * @Param [courseId, teaId, request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/sendApply")
    public ResultResponse sendApply(@RequestParam("courseId") String courseId
            ,@RequestParam("teaId") Integer teaId
            ,HttpServletRequest request){
        String token = request.getHeader("token");
        String userId = TokenUtil.getUserId(token);
        return studentService.sendApply(userId,courseId,teaId);
    }

    /**
     * @Author qiang
     * @Description //TODO 查询当前学生所选课程以及考试信息
     * @Date 10:15 2020/11/24
     * @Param [request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/selectCourseInfo")
    public ResultResponse selectCourseInfo(HttpServletRequest request){
        String token = request.getHeader("token");
        String userId = TokenUtil.getUserId(token);
        return studentService.selectCourse(userId);
    }

    /**
     * @Author qiang
     * @Description //TODO 开始考试并查看试卷内容
     * @Date 10:54 2020/11/24
     * @Param [examId]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/startExam")
    public ResultResponse startExam(@RequestParam("examId") String examId){
        return studentService.startExam(examId);
    }

}
