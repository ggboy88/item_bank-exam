package com.ggboy.exam.config;

import com.ggboy.exam.interceptor.AuthInterceptor;
import com.ggboy.exam.interceptor.LogInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * @Author qiang
 * @Description //TODO 拦截器注册
 * @Date 16:38 2020/10/28
 */
@Configuration
public class AuthConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //拦截器注册
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**");
        InterceptorRegistration registration = registry.addInterceptor(
                getInterceptor());
        registration.addPathPatterns("/**");
        registration.excludePathPatterns("/auth/**")
                .excludePathPatterns("/course/selectSpecialty")
                .excludePathPatterns("/auth/tea/login")
                .excludePathPatterns("/auth/stu/login","/auth/stu/register");

    }

    @Bean
    public AuthInterceptor getInterceptor(){
        return new AuthInterceptor();
    }


}
