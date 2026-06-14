package com.example.uav.service.impl;

import com.example.uav.domain.dto.UavDTO;
import com.example.uav.service.AiAttributeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * AI 属性生成服务实现（规则策略）。
 * 根据型号关键词匹配无人机等级，生成合理的属性范围内的随机值。
 * 如需接入外部 AI API，替换 generateAttributes 方法实现即可。
 */
@Slf4j
@Service
public class AiAttributeServiceImpl implements AiAttributeService {

    private static final Random RANDOM = new Random();

    /**
     * 根据无人机型号关键词生成属性。
     * 支持三种等级：消费级、工业级、军用级（默认消费级）。
     *
     * @param model 无人机型号描述
     * @return 填充了属性字段的 UavDTO（id 为 null）
     */
    @Override
    public UavDTO generateAttributes(String model) {
        log.info("AI 属性生成，型号={}", model);
        UavGrade grade = detectGrade(model);
        return buildDto(model, grade);
    }

    /**
     * 根据型号关键词推断无人机等级。
     *
     * @param model 型号描述
     * @return 无人机等级
     */
    private UavGrade detectGrade(String model) {
        if (model == null) {
            return UavGrade.CONSUMER;
        }
        String lower = model.toLowerCase();
        if (lower.contains("军用") || lower.contains("military") || lower.contains("combat")) {
            return UavGrade.MILITARY;
        }
        if (lower.contains("工业") || lower.contains("industrial") || lower.contains("enterprise")) {
            return UavGrade.INDUSTRIAL;
        }
        return UavGrade.CONSUMER;
    }

    /**
     * 根据等级在合理范围内随机生成属性。
     *
     * @param model 型号描述
     * @param grade 无人机等级
     * @return UavDTO
     */
    private UavDTO buildDto(String model, UavGrade grade) {
        return UavDTO.builder()
                .model(model)
                .manufacturer("AI 自动生成")
                .maxPayload(randomDouble(grade.minPayload, grade.maxPayload))
                .maxAltitude(randomInt(grade.minAltitude, grade.maxAltitude))
                .maxFlightTime(randomInt(grade.minFlightTime, grade.maxFlightTime))
                .maxSpeed(randomDouble(grade.minSpeed, grade.maxSpeed))
                .wingspan(randomDouble(grade.minWingspan, grade.maxWingspan))
                .weight(randomDouble(grade.minWeight, grade.maxWeight))
                .status(1)
                .aiGenerated(1)
                .remark("由 AI 规则引擎自动生成，等级：" + grade.label)
                .build();
    }

    private double randomDouble(double min, double max) {
        return Math.round((min + RANDOM.nextDouble() * (max - min)) * 10.0) / 10.0;
    }

    private int randomInt(int min, int max) {
        return min + RANDOM.nextInt(max - min + 1);
    }

    /**
     * 无人机等级及对应属性范围枚举。
     */
    private enum UavGrade {
        CONSUMER("消费级",  0.5,  5.0,  100, 3000,   20, 60,  8,  25,  30, 120,   0.5, 3.0),
        INDUSTRIAL("工业级", 5.0, 30.0, 2000, 6000,   40, 120, 15, 50,  80, 250,   3.0, 15.0),
        MILITARY("军用级", 30.0, 200.0, 5000, 20000, 120, 480, 50, 300, 150, 600, 15.0, 80.0);

        final String label;
        final double minPayload, maxPayload;
        final int minAltitude, maxAltitude;
        final int minFlightTime, maxFlightTime;
        final double minSpeed, maxSpeed;
        final double minWingspan, maxWingspan;
        final double minWeight, maxWeight;

        UavGrade(String label,
                 double minPayload, double maxPayload,
                 int minAltitude, int maxAltitude,
                 int minFlightTime, int maxFlightTime,
                 double minSpeed, double maxSpeed,
                 double minWingspan, double maxWingspan,
                 double minWeight, double maxWeight) {
            this.label = label;
            this.minPayload = minPayload; this.maxPayload = maxPayload;
            this.minAltitude = minAltitude; this.maxAltitude = maxAltitude;
            this.minFlightTime = minFlightTime; this.maxFlightTime = maxFlightTime;
            this.minSpeed = minSpeed; this.maxSpeed = maxSpeed;
            this.minWingspan = minWingspan; this.maxWingspan = maxWingspan;
            this.minWeight = minWeight; this.maxWeight = maxWeight;
        }
    }
}
