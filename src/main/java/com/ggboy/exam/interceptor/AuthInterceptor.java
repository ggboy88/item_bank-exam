package com.ggboy.exam.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.ggboy.exam.common.ResultEnum;
import com.ggboy.exam.utils.RedisUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * @Author qiang
 * @Description //TODO 登录认证拦截器
 * @Date 16:38 2020/10/28
 */
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    private RedisUtils redisUtils;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("token");
        String user = request.getHeader("user");
        JSONObject json = new JSONObject();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        if (token == null){
            json.put("code", ResultEnum.NO_LOGIN.getCode());
            json.put("message",ResultEnum.NO_LOGIN.getMessage());
            json.put("success",false);
            json.put("data",null);
            out = response.getWriter();
            out.append(json.toString());
            return false;
        }
        String token1 = String.valueOf(redisUtils.get(user));

        if (!token.equals(token1)){
            json.put("code",ResultEnum.LOGIN_EXPIRE.getCode());
            json.put("message",ResultEnum.LOGIN_EXPIRE.getMessage());
            json.put("success",false);
            out = response.getWriter();
            out.append(json.toString());
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
