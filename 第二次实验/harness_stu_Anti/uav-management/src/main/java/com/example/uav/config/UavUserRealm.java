package com.example.uav.config;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

/**
 * 自定义 Shiro Realm，负责认证与授权逻辑。
 * 当前使用固定账号演示，生产环境应接入数据库用户表。
 */
@Component
public class UavUserRealm extends AuthorizingRealm {

    /** 演示用固定账号（生产环境替换为数据库查询） */
    private static final String DEMO_USERNAME = "admin";
    private static final String DEMO_PASSWORD = "admin123";

    /**
     * 授权：返回当前用户的角色和权限信息。
     *
     * @param principals 身份信息集合
     * @return 授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole("admin");
        info.addStringPermission("uav:*");
        return info;
    }

    /**
     * 认证：验证用户名和密码是否正确。
     *
     * @param token 登录凭证（用户名+密码）
     * @return 认证信息
     * @throws AuthenticationException 认证失败时抛出
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();

        if (!DEMO_USERNAME.equals(username)) {
            throw new UnknownAccountException("用户不存在");
        }
        // 返回认证信息，Shiro 会自动比对密码
        return new SimpleAuthenticationInfo(username, DEMO_PASSWORD, getName());
    }
}
