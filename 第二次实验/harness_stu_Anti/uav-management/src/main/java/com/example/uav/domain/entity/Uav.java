package com.example.uav.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 无人机信息实体，对应数据库表 t_uav。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Uav {

    /** 主键 */
    private Long id;

    /** 注册编号（唯一） */
    private String uavCode;

    /** 型号 */
    private String model;

    /** 制造商 */
    private String manufacturer;

    /** 最大载重（kg） */
    private Double maxPayload;

    /** 最大飞行高度（m） */
    private Integer maxAltitude;

    /** 最大续航时长（min） */
    private Integer maxFlightTime;

    /** 最大速度（m/s） */
    private Double maxSpeed;

    /** 翼展（cm） */
    private Double wingspan;

    /** 自重（kg） */
    private Double weight;

    /** 状态：1-正常，0-停用 */
    private Integer status;

    /** 备注 */
    private String remark;

    /** 是否 AI 生成：0-手动，1-AI 生成 */
    private Integer aiGenerated;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    /** 逻辑删除：0-正常，1-已删除 */
    private Integer deleted;
}
