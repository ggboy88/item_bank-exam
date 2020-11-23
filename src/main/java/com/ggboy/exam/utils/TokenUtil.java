package com.ggboy.exam.utils;

public class TokenUtil {

    public static String getUserId(String token){
        String[] split = token.split("\\.");
        return split[0];
    }

    public static String getAuthToken(String token){
        return token.split("\\.")[1];
    }

}
