package com.md.basePlatform.service;

/**
 * DroneService - 无人机服务层接口
 * 
 * 【功能】：定义无人机业务操作规范
 * 【方法】：CRUD + AI属性生成 + 模糊搜索
 * 【设计】：接口与实现分离，面向接口编程
 * 【依赖】：依赖DroneRepository进行数据访问
 */

import com.md.basePlatform.domain.Drone;
import java.util.List;

public interface DroneService {
    List<Drone> findAll();
    Drone findById(Integer id);
    void save(Drone drone);
    void update(Drone drone);
    void delete(Integer id);
    Drone generateDroneProperties();
    List<Drone> search(String keyword);
}