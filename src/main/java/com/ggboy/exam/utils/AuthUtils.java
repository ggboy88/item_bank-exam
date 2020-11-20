package com.ggboy.exam.utils;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AuthUtils {

    @Resource
    private RedisUtils redisUtils;

    public String getAuthToken(String id,String username,String password){
        long l = System.currentTimeMillis();
        String token = MD5Util.MD5(l + ":" + username + password);
        redisUtils.setex(id,token,1800);
        return token;
    }


}
