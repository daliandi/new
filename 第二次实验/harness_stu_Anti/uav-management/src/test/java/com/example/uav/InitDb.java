package com.example.uav;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class InitDb {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false";
        String user = "root";
        String pass = "root";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 Statement stmt = conn.createStatement()) {
                
                String sqlFile = "docs/init-mysql.sql";
                String content = new String(Files.readAllBytes(Paths.get(sqlFile)), "UTF-8");
                String[] queries = content.split(";");
                
                for (String q : queries) {
                    if (q.trim().isEmpty()) continue;
                    System.out.println("Executing: " + q.trim().substring(0, Math.min(50, q.trim().length())) + "...");
                    stmt.execute(q.trim());
                }
                System.out.println("Database initialized successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
