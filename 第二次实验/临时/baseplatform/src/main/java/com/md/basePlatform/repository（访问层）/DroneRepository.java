package com.md.basePlatform.repository;

/**
 * DroneRepository - 无人机数据访问接口
 * 
 * 【功能】：无人机数据访问操作
 * 【继承】：继承BaseRepository获得通用CRUD方法
 * 【实现】：由DroneRepositoryImpl实现
 * 【底层】：通过MyBatis的DroneMapper操作数据库
 */

import com.md.basePlatform.domain.Drone;

public interface DroneRepository extends BaseRepository<Drone> {
}