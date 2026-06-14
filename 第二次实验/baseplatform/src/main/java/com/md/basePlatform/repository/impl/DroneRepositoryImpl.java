package com.md.basePlatform.repository.impl;

/**
 * DroneRepositoryImpl - 无人机数据访问实现类
 * 
 * 【功能】：实现DroneRepository接口
 * 【继承】：继承BaseRepositoryImpl获得通用CRUD实现
 * 【指定Mapper】：重写getMapperClass()返回DroneMapper.class
 * 【注解】：@Repository标识为Spring组件
 */

import com.md.basePlatform.mapper.DroneMapper;
import com.md.basePlatform.repository.DroneRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DroneRepositoryImpl extends BaseRepositoryImpl<com.md.basePlatform.domain.Drone> implements DroneRepository {

    @Override
    protected Class<?> getMapperClass() {
        return DroneMapper.class;
    }
}