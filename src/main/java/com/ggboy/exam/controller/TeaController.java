package com.ggboy.exam.controller;

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
    public ResultResponse courseManage(HttpServletRequest request){
        String token = request.getHeader("token");
        String userId = TokenUtil.getUserId(token);
        return teaService.getTeaCourse(userId);
    }

    /**
     * @Author qiang
     * @Description //TODO 添加当前学院课程
     * @Date 9:49 2020/11/4
     * @Param [courseIds, request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @PostMapping("/addCourse")
    public ResultResponse addCourse(@RequestBody List<String> courseIds,HttpServletRequest request){
        String token = request.getHeader("token");
        String user = TokenUtil.getUserId(token);
        return teaService.addCourse(Integer.parseInt(user),courseIds);
    }

    /**
     * @Author qiang
     * @Description //TODO 查询当前用户当前课程下所有学生信息
     * @Date 14:44 2020/11/4
     * @Param [courseId, request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/searchStu")
    public ResultResponse searchStu(@RequestParam("courseId") String courseId,HttpServletRequest request){
        String token = request.getHeader("token");
        String user = TokenUtil.getUserId(token);
        return teaService.searchCourseStu(courseId,user);
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

}
