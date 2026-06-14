package com.example.uav.config;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Apache Shiro 安全配置，全注解方式，无需 XML。
 */
@Configuration
public class ShiroConfig {

    /**
     * 配置 Shiro 过滤器链及登录/成功/未授权路径。
     *
     * @param securityManager 安全管理器
     * @return ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        factoryBean.setLoginUrl("/login");
        factoryBean.setSuccessUrl("/uav/list");
        factoryBean.setUnauthorizedUrl("/403");

        // 过滤器链（顺序重要，LinkedHashMap 保证顺序）
        Map<String, String> filterChainMap = new LinkedHashMap<>();
        filterChainMap.put("/static/**", "anon");
        filterChainMap.put("/css/**", "anon");
        filterChainMap.put("/js/**", "anon");
        filterChainMap.put("/favicon.ico", "anon");
        filterChainMap.put("/druid/**", "anon");
        filterChainMap.put("/login", "anon");
        filterChainMap.put("/api/login", "anon");
        filterChainMap.put("/logout", "logout");
        filterChainMap.put("/uav/**", "authc");
        filterChainMap.put("/api/**", "authc");
        filterChainMap.put("/**", "authc");
        factoryBean.setFilterChainDefinitionMap(filterChainMap);
        return factoryBean;
    }

    /**
     * 配置 SecurityManager，注入自定义 Realm。
     *
     * @param realm 自定义 Realm
     * @return SecurityManager
     */
    @Bean
    public DefaultWebSecurityManager securityManager(AuthorizingRealm realm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(realm);
        manager.setSessionManager(sessionManager());
        return manager;
    }

    /**
     * 配置 Session 管理器，设置超时时间为 30 分钟。
     *
     * @return DefaultWebSessionManager
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 30分钟超时（毫秒）
        sessionManager.setGlobalSessionTimeout(30 * 60 * 1000L);
        sessionManager.setDeleteInvalidSessions(true);
        return sessionManager;
    }
}
