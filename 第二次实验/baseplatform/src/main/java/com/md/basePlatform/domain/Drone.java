package com.md.basePlatform.domain;

/**
 * Drone - 无人机实体类（POJO/Entity）
 * 
 * 【对应表】：drone表
 * 【字段】：id/name/model/manufacturer/serialNumber/flightTime/status/createdDate/updatedDate
 * 【规范】：遵循JavaBean规范，私有字段+getter/setter
 * 【验证】：使用Hibernate Validation进行数据校验
 * 【用途】：承载无人机数据，在各层之间传递
 */

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Date;

public class Drone {
    private Integer id;
    
    @NotBlank(message = "名称不能为空")
    @Size(max = 100, message = "名称长度不能超过100个字符")
    private String name;
    
    @NotBlank(message = "型号不能为空")
    @Size(max = 50, message = "型号长度不能超过50个字符")
    private String model;
    
    @NotBlank(message = "制造商不能为空")
    @Size(max = 100, message = "制造商长度不能超过100个字符")
    private String manufacturer;
    
    @NotBlank(message = "序列号不能为空")
    @Size(max = 50, message = "序列号长度不能超过50个字符")
    private String serialNumber;
    
    @NotNull(message = "飞行时间不能为空")
    @Positive(message = "飞行时间必须为正数")
    private Double flightTime;
    
    @NotBlank(message = "状态不能为空")
    private String status;
    
    private Date createdDate;
    private Date updatedDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Double getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(Double flightTime) {
        this.flightTime = flightTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}