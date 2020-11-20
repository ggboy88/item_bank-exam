package com.ggboy.exam.controller;

import com.ggboy.exam.common.ResultEnum;
import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.service.TeaService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        String userId = request.getHeader("user");
        if (userId == null){
            return ResultResponse.fail(ResultEnum.PARAM_ERROR);
        }
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
    public ResultResponse addCourse(List<Integer> courseIds,HttpServletRequest request){
        String user = request.getHeader("user");
        if (user == null){
            return ResultResponse.fail(ResultEnum.PARAM_ERROR);
        }
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
        String user = request.getHeader("user");
        if (user == null){
            return ResultResponse.fail(ResultEnum.PARAM_ERROR);
        }
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
    public ResultResponse deleteStu(@RequestParam("stuId") String stuId,@RequestParam("courseId") Integer courseId){
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

}
