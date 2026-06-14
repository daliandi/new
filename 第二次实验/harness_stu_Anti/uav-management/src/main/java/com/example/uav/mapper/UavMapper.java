package com.example.uav.mapper;

import com.example.uav.domain.entity.Uav;
import com.example.uav.domain.query.UavQuery;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 无人机数据访问层接口（MyBatis Mapper）。
 * 简单操作使用注解 SQL；复杂查询（分页列表）在 UavMapper.xml 中定义。
 */
@Mapper
public interface UavMapper {

    /**
     * 按条件分页查询无人机列表（XML 中定义，兼顾 MySQL/SQLite）。
     *
     * @param query 查询条件
     * @return 无人机列表（由 PageHelper 自动分页）
     */
    List<Uav> selectList(UavQuery query);

    /**
     * 根据 ID 查询单条无人机信息。
     *
     * @param id 无人机主键
     * @return 无人机实体，不存在则返回 null
     */
    @Select("SELECT * FROM t_uav WHERE id = #{id} AND deleted = 0")
    Uav selectById(Long id);

    /**
     * 根据注册编号查询无人机（用于唯一性校验）。
     *
     * @param uavCode 注册编号
     * @return 无人机实体，不存在则返回 null
     */
    @Select("SELECT * FROM t_uav WHERE uav_code = #{uavCode} AND deleted = 0")
    Uav selectByUavCode(String uavCode);

    /**
     * 新增无人机记录。
     *
     * @param uav 无人机实体（id 由数据库自增生成）
     * @return 影响行数
     */
    @Insert("INSERT INTO t_uav (uav_code, model, manufacturer, max_payload, max_altitude, " +
            "max_flight_time, max_speed, wingspan, weight, status, remark, ai_generated, " +
            "created_at, updated_at, deleted) VALUES " +
            "(#{uavCode}, #{model}, #{manufacturer}, #{maxPayload}, #{maxAltitude}, " +
            "#{maxFlightTime}, #{maxSpeed}, #{wingspan}, #{weight}, #{status,jdbcType=INTEGER}, " +
            "#{remark}, #{aiGenerated}, #{createdAt}, #{updatedAt}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Uav uav);

    /**
     * 根据 ID 更新无人机信息（XML 中定义动态 SQL）。
     *
     * @param uav 包含更新字段的无人机实体（id 必须不为 null）
     * @return 影响行数
     */
    int updateById(Uav uav);

    /**
     * 根据 ID 逻辑删除无人机（设置 deleted=1）。
     *
     * @param id 无人机主键
     * @return 影响行数
     */
    @Update("UPDATE t_uav SET deleted = 1, updated_at = NOW() WHERE id = #{id} AND deleted = 0")
    int deleteById(Long id);
}
