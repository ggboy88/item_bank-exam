package com.ggboy.exam.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ggboy.exam.beans.vo.CourseReqVo;
import com.ggboy.exam.beans.vo.UpdateUserVo;
import com.ggboy.exam.common.ResultEnum;
import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.service.TeaService;
import com.ggboy.exam.utils.TokenUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

        List<String> courseId = JSONObject.parseArray(JSON.toJSONString(courseIds), String.class);

        String token = request.getHeader("token");
        String user = TokenUtil.getUserId(token);
        return teaService.addCourse(Integer.parseInt(user),courseId);
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
     * @Description //TODO 清空当前选择课程所有学生信息
     * @Date 11:14 2020/12/28
     * @Param [courseId, request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/clearAll")
    public ResultResponse clearAllStu(@RequestParam("courseId") String courseId,HttpServletRequest request){
        String token = request.getHeader("token");
        String user = TokenUtil.getUserId(token);
        return teaService.clearAllStu(courseId,user);
    }

    /**
     * @Author qiang
     * @Description //TODO 删除当前课程学生信息
     * @Date 14:47 2020/11/4
     * @Param [stuId, courseId]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/deleteStu")
    public ResultResponse deleteStu(@RequestParam("stuId") String stuId,@RequestParam("courseId") String courseId,HttpServletRequest request){
        String token = request.getHeader("token");
        String user = TokenUtil.getUserId(token);
        return teaService.deleteCourseStu(stuId,courseId,user);
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

    /**
     * @Author qiang
     * @Description //TODO 查看个人信息
     * @Date 14:07 2020/12/28
     * @Param [request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @GetMapping("/getOwnMsg")
    public ResultResponse getOwnMsg(HttpServletRequest request){
        String token = request.getHeader("token");
        String userId = TokenUtil.getUserId(token);
        return teaService.getOwnMsg(userId);
    }

    /**
     * @Author qiang
     * @Description //TODO 更新用户数据
     * @Date 14:51 2021/1/13
     * @Param [updateUser, request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @PostMapping("/updateUser")
    public ResultResponse updateUser(@RequestBody JSONObject updateUser,HttpServletRequest request){
        String token = request.getHeader("token");
        String userId = TokenUtil.getUserId(token);
        UpdateUserVo updateUserVo = updateUser.toJavaObject(UpdateUserVo.class);
        return teaService.updateUser(updateUserVo,userId);
    }

    /**
     * @Author qiang
     * @Description //TODO 头像上传
     * @Date 14:51 2021/1/13
     * @Param [file, request]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @PostMapping("/uploadHead")
    public ResultResponse uploadHead(@RequestParam("file") MultipartFile file,HttpServletRequest request){
        String token = request.getHeader("token");
        String userId = TokenUtil.getUserId(token);
        return teaService.uploadHead(file,userId);
    }

}
