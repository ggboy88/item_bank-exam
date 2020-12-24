package com.ggboy.exam.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ggboy.exam.beans.vo.CourseReqVo;
import com.ggboy.exam.common.ResultEnum;
import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.service.TeaService;
import com.ggboy.exam.utils.TokenUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author qiang
 * @Description //TODO 教师相关功能接口
 * @Date 11:12 2020/11/20
 */
@RestController
@RequestMapping("/tea")
public class TeaController {

    @Resource
    private TeaService teaService;

    /**
     * @Author qiang
     * @Description //TODO 查询当前已拥有课程信息
     * @Date 9:49 2020/11/4
     * @Param [request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/getCourse")
    public ResultResponse courseManage(HttpServletRequest request,
                                       @RequestParam(name = "pageSize",defaultValue = "5")Integer pageSize,
                                       @RequestParam(name = "pageNum",defaultValue = "1")Integer pageNum){
        String token = request.getHeader("token");
        String userId = TokenUtil.getUserId(token);
        return teaService.getTeaCourse(userId,pageSize,pageNum);
    }
    
    /**
     * @Author qiang
     * @Description //TODO 逻辑删除当前教师课程
     * @Date 17:01 2020/12/3
     * @Param [courseId, request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/delete/course")
    public ResultResponse deleteCourse(@RequestParam("courseId")String courseId, HttpServletRequest request){

        String token = request.getHeader("token");
        String userId = TokenUtil.getUserId(token);
        return teaService.deleteTeaCourse(userId,courseId);
    }

    /**
     * @Author qiang
     * @Description //TODO 添加当前学院课程
     * @Date 9:49 2020/11/4
     * @Param [courseIds, request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @PostMapping("/addCourse")
    public ResultResponse addCourse(@RequestBody JSONArray courseIds, HttpServletRequest request){

        System.out.println(courseIds);
        List<String> courseId = JSONObject.parseArray(JSON.toJSONString(courseIds), String.class);

        String token = request.getHeader("token");
        String user = TokenUtil.getUserId(token);
        return teaService.addCourse(Integer.parseInt(user),courseId);
    }

    /**
     * @Author qiang
     * @Description //TODO 查询当前用户拥有课程下所有学生信息
     * @Date 14:44 2020/11/4
     * @Param [courseId, request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/searchStu")
    public ResultResponse searchStu(HttpServletRequest request){
        String token = request.getHeader("token");
        String user = TokenUtil.getUserId(token);
        return teaService.searchCourseStu(user);
    }

    /**
     * @Author qiang
     * @Description //TODO 删除当前课程学生信息
     * @Date 14:47 2020/11/4
     * @Param [stuId, courseId]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/deleteStu")
    public ResultResponse deleteStu(@RequestParam("stuId") String stuId,@RequestParam("courseId") String courseId){
        return teaService.deleteCourseStu(stuId,courseId);
    }
    
    /**
     * @Author qiang
     * @Description //TODO 同意/拒绝学生申请加入课程
     * @Date 14:55 2020/11/4
     * @Param []
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/accessStuApply")
    public ResultResponse accessStuApply(@RequestParam("access") Boolean access,@RequestParam("accessId") String accessId){
        return teaService.accessStuApply(access,accessId);
    }
    
    /**
     * @Author qiang
     * @Description //TODO 查询学生申请加入课程信息
     * @Date 16:26 2020/11/20
     * @Param [request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/selectStuApply")
    public ResultResponse selectApply(HttpServletRequest request
            ,@RequestParam(name = "pageNum",defaultValue = "1") Integer pageNum
            ,@RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        String token = request.getHeader("token");
        String userId = TokenUtil.getUserId(token);
        return teaService.selectStuApply(Integer.parseInt(userId),pageNum,pageSize);
    }
    
    /**
     * @Author qiang
     * @Description //TODO 查询学生申请未处理数
     * @Date 15:04 2020/12/3
     * @Param [request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/selectStuApplyAccount")
    public ResultResponse selectStuApplyAccount(HttpServletRequest request){
        String token = request.getHeader("token");
        String userId = TokenUtil.getUserId(token);
        return teaService.selectStuApplyAccount(Integer.parseInt(userId));
    }

    /**
     * @Author qiang
     * @Description //TODO 查询当前用户信息
     * @Date 15:42 2020/12/2
     * @Param [request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/selectUser")
    public ResultResponse selectUserName(HttpServletRequest request){
        String token = request.getHeader("token");
        String userId = TokenUtil.getUserId(token);
        return teaService.selectUser(Integer.parseInt(userId));
    }

}
