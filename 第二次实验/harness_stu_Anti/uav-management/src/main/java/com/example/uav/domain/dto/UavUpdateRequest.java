package com.example.uav.domain.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 修改无人机请求 DTO。
 */
@Data
public class UavUpdateRequest {

    /** 主键，必填 */
    @NotNull(message = "ID不能为空")
    private Long id;

    /** 型号，不能为空 */
    @NotBlank(message = "型号不能为空")
    @Size(max = 100, message = "型号最大100个字符")
    private String model;

    /** 制造商 */
    @Size(max = 100, message = "制造商最大100个字符")
    private String manufacturer;

    /** 最大载重（kg） */
    @Min(value = 0, message = "最大载重不能为负数")
    private Double maxPayload;

    /** 最大飞行高度（m） */
    @Min(value = 0, message = "最大飞行高度不能为负数")
    private Integer maxAltitude;

    /** 最大续航时长（min） */
    @Min(value = 0, message = "最大续航时长不能为负数")
    private Integer maxFlightTime;

    /** 最大速度（m/s） */
    @Min(value = 0, message = "最大速度不能为负数")
    private Double maxSpeed;

    /** 翼展（cm） */
    @Min(value = 0, message = "翼展不能为负数")
    private Double wingspan;

    /** 自重（kg） */
    @Min(value = 0, message = "自重不能为负数")
    private Double weight;

    /** 状态：1-正常，0-停用 */
    private Integer status;

    /** 备注 */
    @Size(max = 500, message = "备注最大500个字符")
    private String remark;
}
