package com.md.basePlatform.config;

/**
 * CustomRealm - 自定义Shiro认证域
 * 
 * 【功能】：实现Shiro认证逻辑
 * 【继承】：继承AuthorizingRealm
 * 【认证】：硬编码用户 admin/123456（演示用）
 * 【授权】：暂未实现，返回null
 */

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class CustomRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        
        if ("admin".equals(username) && "123456".equals(password)) {
            return new SimpleAuthenticationInfo(username, password, getName());
        }
        
        return null;
    }
}