package com.md.basePlatform.config;

/**
 * WebMvcConfig - Spring MVC配置类
 * 
 * 【功能】：注册拦截器等Spring MVC配置
 * 【注解】：@Configuration标识为配置类
 * 【接口】：实现WebMvcConfigurer接口
 * 【注册】：addInterceptors注册RequestInterceptor，拦截所有路径
 */

import com.md.basePlatform.interceptor.RequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor()).addPathPatterns("/**");
    }
}