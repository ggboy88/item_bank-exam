package com.ggboy.exam.controller;

import com.ggboy.exam.beans.ImageVerificationCode;
import com.ggboy.exam.beans.exam.StuRegisterParam;
import com.ggboy.exam.common.ResultEnum;
import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.service.AuthService;
import com.ggboy.exam.utils.RedisUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @Author qiang
 * @Description //TODO 认证功能接口
 * @Date 11:10 2020/11/20
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @Resource
    private RedisUtils redisUtils;

    /**
     * @Author qiang
     * @Description //TODO 教师登录接口
     * @Date 13:59 2020/11/4
     * @Param [username, password]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @PostMapping("/tea/login")
    public ResultResponse teaLogin(@RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   @RequestParam("code") String code,
                                   HttpServletRequest request){
        String ip = request.getRemoteAddr();
        String text = (String) redisUtils.get(ip+"_yzm");
        if (!code.toUpperCase().equalsIgnoreCase(text.toUpperCase())){
            return ResultResponse.fail("验证码错误");
        }
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
    public ResultResponse stuLogin(@RequestParam("phone") String phone,
                                   @RequestParam("password") String password,
                                   @RequestParam("code") String code,
                                   HttpServletRequest request){
        String ip = request.getRemoteAddr();
        String text = (String) redisUtils.get(ip+"_yzm");
        if (!code.toUpperCase().equals(text.toUpperCase())){
            return ResultResponse.fail("验证码错误");
        }
        return authService.stuLogin(phone,password.toUpperCase());
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
    
    /**
     * @Author qiang
     * @Description //TODO 获取验证码图片
     * @Date 15:39 2020/11/23
     * @Param [request, response]
     * @return void
     */
    @GetMapping("/getVerifiCode")
    public void getVerifiCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageVerificationCode ivc = new ImageVerificationCode();     //用我们的验证码类，生成验证码类对象
        BufferedImage image = ivc.getImage();  //获取验证码
        String ip = request.getRemoteAddr();
        System.out.println(ip);
        redisUtils.setex(ip+"_yzm",ivc.getText(),60);
        ImageVerificationCode.output(image, response.getOutputStream());
    }

}
