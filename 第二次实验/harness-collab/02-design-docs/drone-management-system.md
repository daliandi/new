# 无人机信息管理系统技术设计文档

## 1. 系统架构

### 1.1 四层架构
- **Controller层**：处理HTTP请求，调用Service层方法
- **Service层**：实现业务逻辑，调用Repository层方法
- **Repository层**：数据访问层，与数据库交互
- **Domain层**：实体类，对应数据库表结构

### 1.2 目录结构
```
src/main/java/com/md/basePlatform/
├── controller/          # 控制器层
├── service/             # 服务层
│   ├── impl/            # 服务实现
├── repository/          # 数据访问层
│   ├── impl/            # 数据访问实现
├── domain/              # 实体类
├── config/              # 配置类
├── interceptor/         # 拦截器
├── common/              # 通用工具
├── exception/           # 异常处理
└── BasePlatformApplication.java  # 应用入口
```

## 2. 技术实现

### 2.1 依赖管理
- 使用Maven管理依赖
- 集成Spring Boot 2.2.x、MyBatis 3.5.x、Shiro 1.7等框架
- 添加SQLite和MySQL驱动

### 2.2 数据库设计
- **无人机信息表(drone)**：
  | 字段名 | 数据类型 | 描述 |
  | --- | --- | --- |
  | id | INTEGER | 主键，自增 |
  | name | VARCHAR(100) | 无人机名称 |
  | model | VARCHAR(100) | 型号 |
  | manufacturer | VARCHAR(100) | 制造商 |
  | serialNumber | VARCHAR(100) | 序列号 |
  | flightTime | DOUBLE | 飞行时间(小时) |
  | status | VARCHAR(50) | 状态 |
  | createdDate | TIMESTAMP | 创建日期 |
  | updatedDate | TIMESTAMP | 更新日期 |

### 2.3 核心功能实现

#### 2.3.1 无人机信息管理
- **DroneController**：处理无人机相关的HTTP请求
- **DroneService**：实现无人机业务逻辑
- **DroneRepository**：实现无人机数据访问
- **Drone**：无人机实体类

#### 2.3.2 数据库切换
- 使用Spring Boot的多数据源配置
- 通过配置文件切换数据库类型

#### 2.3.3 拦截器
- **RequestInterceptor**：拦截所有请求，打印请求信息

## 3. 接口设计

### 3.1 RESTful API
- **GET /api/drones**：获取所有无人机信息
- **GET /api/drones/{id}**：获取指定无人机信息
- **POST /api/drones**：添加新无人机
- **PUT /api/drones/{id}**：更新无人机信息
- **DELETE /api/drones/{id}**：删除无人机信息

### 3.2 页面接口
- **GET /drones**：无人机列表页面
- **GET /drones/add**：添加无人机页面
- **GET /drones/edit/{id}**：编辑无人机页面
- **POST /drones/save**：保存无人机信息
- **GET /drones/delete/{id}**：删除无人机

## 4. 测试设计

### 4.1 单元测试
- **Service层**：使用Mockito进行测试
- **Controller层**：使用@WebMvcTest进行测试
- **Repository层**：使用@DataJpaTest进行测试

### 4.2 集成测试
- 测试数据库切换功能
- 测试拦截器功能
- 测试完整的业务流程

## 5. 部署方案

### 5.1 环境要求
- JDK 8+
- Maven 3+
- SQLite/MySQL

### 5.2 配置文件
- application.properties：主配置文件
- application-sqlite.properties：SQLite配置
- application-mysql.properties：MySQL配置

### 5.3 启动命令
```bash
mvn spring-boot:run -Dspring.profiles.active=sqlite
```

## 6. 安全设计

### 6.1 权限管理
- 使用Apache Shiro进行权限控制
- 配置基于角色的访问控制

### 6.2 数据安全
- 使用参数化查询防止SQL注入
- 对敏感数据进行加密处理

## 7. 性能优化

### 7.1 数据库优化
- 使用索引提高查询性能
- 合理设计表结构

### 7.2 代码优化
- 使用缓存减少数据库访问
- 优化SQL查询语句

## 8. 技术风险

### 8.1 风险点
- 数据库切换可能导致数据结构不兼容
- 无人机属性自动生成的准确性

### 8.2 应对措施
- 制定数据库迁移策略
- 对自动生成的属性进行验证