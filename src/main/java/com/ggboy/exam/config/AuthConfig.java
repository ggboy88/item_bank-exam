package com.ggboy.exam.config;

import com.ggboy.exam.interceptor.AuthInterceptor;
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
        InterceptorRegistration registration = registry.addInterceptor(
                getInterceptor());
        registration.addPathPatterns("/**");
        registration.excludePathPatterns("/auth/**",
                "/course/selectSpecialty",
                "/auth/stu/**");
    }

    @Bean
    public AuthInterceptor getInterceptor(){
        return new AuthInterceptor();
    }

}
