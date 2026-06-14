package com.md.basePlatform;

/**
 * BasePlatformApplication - Spring Boot应用启动类
 * 
 * 【功能】：项目入口，启动Spring Boot应用
 * 【注解】：@SpringBootApplication开启自动配置
 * 【扫描】：@MapperScan扫描mapper包
 * 【运行】：main方法启动应用
 */

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@MapperScan("com.md.basePlatform.mapper")
public class BasePlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasePlatformApplication.class, args);
	}

}
