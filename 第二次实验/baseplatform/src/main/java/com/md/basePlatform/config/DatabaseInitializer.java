package com.md.basePlatform.config;

/**
 * DatabaseInitializer - 数据库初始化器
 * 
 * 【功能】：应用启动时自动创建drone表
 * 【接口】：实现CommandLineRunner接口
 * 【执行】：run()方法在应用启动后执行
 * 【特性】：CREATE TABLE IF NOT EXISTS 避免重复创建
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            // 创建drone表
            String createTableSQL = "CREATE TABLE IF NOT EXISTS drone (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(100) NOT NULL," +
                "model VARCHAR(100) NOT NULL," +
                "manufacturer VARCHAR(100) NOT NULL," +
                "serialNumber VARCHAR(100) NOT NULL," +
                "flightTime DOUBLE DEFAULT 0," +
                "status VARCHAR(50) NOT NULL," +
                "createdDate TIMESTAMP NOT NULL," +
                "updatedDate TIMESTAMP NOT NULL" +
                ")";
            statement.executeUpdate(createTableSQL);
            System.out.println("Database table created successfully");
        }
    }
}