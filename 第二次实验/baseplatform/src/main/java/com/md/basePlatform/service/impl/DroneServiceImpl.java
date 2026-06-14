package com.md.basePlatform.service.impl;

/**
 * DroneServiceImpl - 无人机服务层实现类
 * 
 * 【功能】：无人机CRUD业务逻辑 + AI生成随机属性 + 模糊搜索
 * 【技术】：@Service注解 + @Autowired依赖注入
 * 【设计】：接口与实现分离，便于测试和替换
 * 【审计】：save/update自动设置createdDate/updatedDate
 */

import com.md.basePlatform.domain.Drone;
import com.md.basePlatform.repository.DroneRepository;
import com.md.basePlatform.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class DroneServiceImpl implements DroneService {

    @Autowired
    private DroneRepository droneRepository;

    @Override
    public List<Drone> findAll() {
        return droneRepository.findAll();
    }

    @Override
    public Drone findById(Integer id) {
        return droneRepository.findById(id);
    }

    @Override
    public void save(Drone drone) {
        drone.setCreatedDate(new Date());
        drone.setUpdatedDate(new Date());
        droneRepository.save(drone);
    }

    @Override
    public void update(Drone drone) {
        drone.setUpdatedDate(new Date());
        droneRepository.update(drone);
    }

    @Override
    public void delete(Integer id) {
        droneRepository.delete(id);
    }

    @Override
    public Drone generateDroneProperties() {
        Drone drone = new Drone();
        Random random = new Random();

        // 生成随机名称
        String[] names = {"Eagle", "Falcon", "Hawk", "Sparrow", "Owl"};
        drone.setName(names[random.nextInt(names.length)] + "-" + (1000 + random.nextInt(9000)));

        // 生成随机型号
        String[] models = {"M1", "M2", "M3", "M4", "M5"};
        drone.setModel(models[random.nextInt(models.length)]);

        // 生成随机制造商
        String[] manufacturers = {"DJI", "Parrot", "Autel", "Yuneec", "Skydio"};
        drone.setManufacturer(manufacturers[random.nextInt(manufacturers.length)]);

        // 生成随机序列号
        drone.setSerialNumber("SN-" + (100000 + random.nextInt(900000)));

        // 生成随机飞行时间
        drone.setFlightTime(Math.round(random.nextDouble() * 1000 * 100.0) / 100.0);

        // 生成随机状态
        String[] statuses = {"运行中", "维护中", "已退役", "已损坏"};
        drone.setStatus(statuses[random.nextInt(statuses.length)]);

        return drone;
    }

    @Override
    public List<Drone> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        return droneRepository.search("%" + keyword.trim() + "%");
    }
}