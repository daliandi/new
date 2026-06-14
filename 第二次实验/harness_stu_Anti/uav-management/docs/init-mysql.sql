-- MySQL 初始化脚本
CREATE DATABASE IF NOT EXISTS uav_db DEFAULT CHARACTER SET utf8mb4;
USE uav_db;

CREATE TABLE IF NOT EXISTS `t_uav` (
  `id`              BIGINT        NOT NULL AUTO_INCREMENT        COMMENT '主键',
  `uav_code`        VARCHAR(64)   NOT NULL                       COMMENT '注册编号（唯一）',
  `model`           VARCHAR(100)  NOT NULL                       COMMENT '型号',
  `manufacturer`    VARCHAR(100)                                 COMMENT '制造商',
  `max_payload`     DOUBLE                                       COMMENT '最大载重(kg)',
  `max_altitude`    INT                                          COMMENT '最大飞行高度(m)',
  `max_flight_time` INT                                          COMMENT '最大续航时长(min)',
  `max_speed`       DOUBLE                                       COMMENT '最大速度(m/s)',
  `wingspan`        DOUBLE                                       COMMENT '翼展(cm)',
  `weight`          DOUBLE                                       COMMENT '自重(kg)',
  `status`          TINYINT       NOT NULL DEFAULT 1             COMMENT '状态:1正常,0停用',
  `remark`          VARCHAR(500)                                 COMMENT '备注',
  `ai_generated`    TINYINT       NOT NULL DEFAULT 0             COMMENT '0手动录入,1AI生成',
  `created_at`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`         TINYINT       NOT NULL DEFAULT 0             COMMENT '逻辑删除:0正常,1已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uav_code` (`uav_code`),
  KEY `idx_model` (`model`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='无人机信息表';

-- 初始演示数据
INSERT INTO `t_uav` (`uav_code`, `model`, `manufacturer`, `max_payload`, `max_altitude`,
                     `max_flight_time`, `max_speed`, `wingspan`, `weight`, `status`, `remark`, `ai_generated`)
VALUES
  ('UAV-2026-001', 'DJI Mini 3 Pro', 'DJI',   0.5, 4000,  34, 16.0,  25.0,  0.249, 1, '消费级迷你无人机', 0),
  ('UAV-2026-002', 'Matrice 300 RTK', 'DJI',  2.7, 5000,  55, 23.0, 895.0,  6.3,   1, '工业级巡检无人机', 0),
  ('UAV-2026-003', '消费级四旋翼-AI', 'AI生成', 1.2, 2000,  28, 14.5,  35.0,  0.8,   1, '由AI规则引擎自动生成', 1);
