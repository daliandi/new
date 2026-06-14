package com.example.uav.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis 配置类。
 * 将 @MapperScan 移至独立配置类，避免在 @WebMvcTest 阶段因为扫描 Mapper 而导致依赖缺失。
 */
@Configuration
@MapperScan("com.example.uav.mapper")
public class MybatisConfig {
}
