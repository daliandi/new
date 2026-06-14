package com.md.basePlatform.interceptor;

/**
 * RequestInterceptor - 请求拦截器
 * 
 * 【功能】：记录HTTP请求和响应日志
 * 【接口】：实现HandlerInterceptor接口
 * 【方法】：preHandle(请求前)、postHandle(Controller后)、afterCompletion(完成后)
 * 【注册】：在WebMvcConfig中注册
 */

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("[RequestInterceptor] " + new Date() + " - Request: " + request.getMethod() + " " + request.getRequestURI());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Do nothing
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("[RequestInterceptor] " + new Date() + " - Response: " + response.getStatus() + " for " + request.getRequestURI());
    }
}