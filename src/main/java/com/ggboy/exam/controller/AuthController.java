package com.ggboy.exam.controller;

import com.ggboy.exam.beans.ImageVerificationCode;
import com.ggboy.exam.beans.exam.StuRegisterParam;
import com.ggboy.exam.common.ResultEnum;
import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.service.AuthService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @GetMapping("/stu/login")
    public ResultResponse stuLogin(@RequestParam("phone") String phone,
                                   @RequestParam("password") String password,
                                   @RequestParam("code") String code,
                                   HttpServletRequest request){
        String text = (String) request.getSession().getAttribute("text");
        if (!code.equals(text)){
            return ResultResponse.fail("验证码错误");
        }
        return authService.stuLogin(phone,password);
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
        request.getSession().setAttribute("text", ivc.getText()); //将验证码的文本存在session中
        ivc.output(image, response.getOutputStream());
    }

}
