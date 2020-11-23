package com.ggboy.exam.controller;

import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.service.StudentService;
import com.ggboy.exam.utils.TokenUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @Description //TODO 查看当前学生可选课程信息
     * @Date 11:22 2020/11/20
     * @Param []
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping
    @RequestMapping("/searchCourse")
    public ResultResponse selectCourse(HttpServletRequest request){
        String token = request.getHeader("token");
        String user = TokenUtil.getUserId(token);
        return studentService.selectCourse(user);
    }

}
