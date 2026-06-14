-- H2 测试数据库 Schema（用于 @MybatisTest）
CREATE TABLE IF NOT EXISTS t_uav (
    id              BIGINT       AUTO_INCREMENT PRIMARY KEY,
    uav_code        VARCHAR(64)  NOT NULL UNIQUE,
    model           VARCHAR(100) NOT NULL,
    manufacturer    VARCHAR(100),
    max_payload     DOUBLE,
    max_altitude    INT,
    max_flight_time INT,
    max_speed       DOUBLE,
    wingspan        DOUBLE,
    weight          DOUBLE,
    status          TINYINT      NOT NULL DEFAULT 1,
    remark          VARCHAR(500),
    ai_generated    TINYINT      NOT NULL DEFAULT 0,
    created_at      DATETIME,
    updated_at      DATETIME,
    deleted         TINYINT      NOT NULL DEFAULT 0
);
