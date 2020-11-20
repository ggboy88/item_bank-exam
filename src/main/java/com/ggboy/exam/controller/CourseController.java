package com.ggboy.exam.controller;

import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.service.CourseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    private CourseService courseService;

    /**
     * @Author qiang
     * @Description //TODO 查询当前教师用户可选择课程
     * @Date 9:51 2020/11/4
     * @Param [request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/selectCourse")
    public ResultResponse selectCourse(HttpServletRequest request){
        String userId = request.getHeader("user");
        return courseService.selectCourseList(userId);
    }

    /**
     * @Author qiang
     * @Description //TODO 学生注册时查询所有学院信息
     * @Date 13:58 2020/11/4
     * @Param []
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/selectSpecialty")
    public ResultResponse selectSpecialty(){
        return courseService.selectSpecialty();
    }


}
