package com.md.basePlatform.mapper;

/**
 * DroneMapper - 无人机数据访问接口（MyBatis注解方式）
 * 
 * 【功能】：定义drone表的CRUD操作和搜索功能
 * 【技术】：@Mapper注解 + @Select/@Insert/@Update/@Delete
 * 【特性】：@Options实现自增主键回填
 * 【映射】：MyBatis自动将SQL结果映射到Drone实体
 */

import com.md.basePlatform.domain.Drone;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DroneMapper {

    @Select("SELECT * FROM drone")
    List<Drone> findAll();

    @Select("SELECT * FROM drone WHERE id = #{id}")
    Drone findById(Integer id);

    @Insert("INSERT INTO drone (name, model, manufacturer, serialNumber, flightTime, status, createdDate, updatedDate) " +
            "VALUES (#{name}, #{model}, #{manufacturer}, #{serialNumber}, #{flightTime}, #{status}, #{createdDate}, #{updatedDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(Drone drone);

    @Update("UPDATE drone SET name = #{name}, model = #{model}, manufacturer = #{manufacturer}, " +
            "serialNumber = #{serialNumber}, flightTime = #{flightTime}, status = #{status}, updatedDate = #{updatedDate} " +
            "WHERE id = #{id}")
    void update(Drone drone);

    @Delete("DELETE FROM drone WHERE id = #{id}")
    void delete(Integer id);

    @Select("SELECT * FROM drone WHERE name LIKE #{keyword} OR model LIKE #{keyword} OR " +
            "manufacturer LIKE #{keyword} OR serialNumber LIKE #{keyword} OR status LIKE #{keyword}")
    List<Drone> search(String keyword);
}