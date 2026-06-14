package com.md.basePlatform.controller;

/**
 * LoginController - 登录控制器
 * 
 * 【功能】：处理用户登录/登出请求
 * 【路由】：GET/POST /login、GET /logout、POST /api/login
 * 【技术】：Apache Shiro安全框架认证
 * 【视图】：login.html登录页面
 * 【API】：支持前后端分离的RESTful登录接口
 */

import com.md.basePlatform.common.R;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "redirect:/drones";
        }
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password, Model model) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        
        try {
            subject.login(token);
            return "redirect:/drones";
        } catch (AuthenticationException e) {
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }
    }

    @PostMapping("/api/login")
    @ResponseBody
    public R<Void> apiLogin(@RequestBody(required = false) LoginRequest loginRequest,
                             @RequestParam(required = false) String username,
                             @RequestParam(required = false) String password) {
        String loginUsername = (loginRequest != null && loginRequest.getUsername() != null) 
            ? loginRequest.getUsername() : username;
        String loginPassword = (loginRequest != null && loginRequest.getPassword() != null) 
            ? loginRequest.getPassword() : password;
        
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginUsername, loginPassword);
        
        try {
            subject.login(token);
            return R.ok("登录成功", null);
        } catch (AuthenticationException e) {
            return R.fail(401, "用户名或密码错误");
        }
    }
    
    public static class LoginRequest {
        private String username;
        private String password;
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
    }

    @GetMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        return "redirect:/login";
    }
}