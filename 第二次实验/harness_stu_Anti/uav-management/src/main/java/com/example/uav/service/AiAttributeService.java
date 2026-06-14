package com.example.uav.service;

import com.example.uav.domain.dto.UavDTO;

/**
 * AI 属性自动生成服务接口。
 */
public interface AiAttributeService {

    /**
     * 根据无人机型号描述自动生成属性。
     * 返回的 DTO 中 id 为 null，供用户确认后再保存。
     *
     * @param model 无人机型号描述（如"消费级四旋翼"、"工业级固定翼"）
     * @return 填充了属性字段的 UavDTO（id 为 null）
     */
    UavDTO generateAttributes(String model);
}
