package com.ggboy.exam.controller;

import com.ggboy.exam.beans.exam.StuRegisterParam;
import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.service.AuthService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    /**
     * @Author qiang
     * @Description //TODO 教师登录接口
     * @Date 13:59 2020/11/4
     * @Param [username, password]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @PostMapping("/tea/login")
    public ResultResponse teaLogin(@RequestParam(value = "username",required = false) String username,
                                    @RequestParam(value = "password",required = false) String password){
        return authService.teaLogin(username,password);
    }

    /**
     * @Author qiang
     * @Description //TODO 学生登录接口
     * @Date 13:59 2020/11/4
     * @Param [username, password]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @PostMapping("/stu/login")
    public ResultResponse stuLogin(@RequestParam("username") String username,
                                   @RequestParam("password") String password){
        return authService.stuLogin(username,password);
    }

    /**
     * @Author qiang
     * @Description //TODO 学生注册接口
     * @Date 13:59 2020/11/4
     * @Param [stuRegisterParam]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @PostMapping("/stu/register")
    public ResultResponse stuRegister(@RequestBody StuRegisterParam stuRegisterParam){
        return authService.stuRegister(stuRegisterParam);
    }

}
