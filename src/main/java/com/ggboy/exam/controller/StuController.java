package com.ggboy.exam.controller;

import com.alibaba.fastjson.JSONObject;
import com.ggboy.exam.beans.TeaCourseLinkResponse;
import com.ggboy.exam.beans.vo.UpdateUserVo;
import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.service.StudentService;
import com.ggboy.exam.utils.TokenUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * @Author qiang
     * @Description //TODO 查询用户数据
     * @Date 15:02 2021/1/13
     * @Param [request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/queryUser")
    public ResultResponse queryStuInfo(HttpServletRequest request){
        String token = request.getHeader("token");
        String userId = TokenUtil.getUserId(token);
        return studentService.queryStu(userId);
    }

    /**
     * @Author qiang
     * @Description //TODO 密码校验
     * @Date 15:02 2021/1/13
     * @Param [password, request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/checkPassword")
    public ResultResponse checkPassword(@RequestParam("password") String password,HttpServletRequest request){
        String token = request.getHeader("token");
        String userId = TokenUtil.getUserId(token);
        return studentService.checkPass(password,userId);
    }

    /**
     * @Author qiang
     * @Description //TODO 更新用户数据
     * @Date 15:02 2021/1/13
     * @Param [updateUser, request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/updateUser")
    public ResultResponse updateUser(@RequestBody JSONObject updateUser,HttpServletRequest request){
        String token = request.getHeader("token");
        String userId = TokenUtil.getUserId(token);
        UpdateUserVo updateUserVo = updateUser.toJavaObject(UpdateUserVo.class);
        return studentService.updateUser(updateUserVo,userId);
    }

    /**
     * @Author qiang
     * @Description //TODO 上传头像
     * @Date 15:28 2021/1/13
     * @Param [file, request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @PostMapping("/uploadHead")
    public ResultResponse uploadHead(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        String token = request.getHeader("token");
        String userId = TokenUtil.getUserId(token);
        return studentService.uploadHead(file,userId);
    }

    /**
     * @Author qiang
     * @Description //TODO 通知
     * @Date 17:01 2021/1/14
     * @Param [request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/getAlarm")
    public ResultResponse alarm(HttpServletRequest request){
        String token = request.getHeader("token");
        String userId = TokenUtil.getUserId(token);
        return studentService.getAlarm(userId);
    }

}
