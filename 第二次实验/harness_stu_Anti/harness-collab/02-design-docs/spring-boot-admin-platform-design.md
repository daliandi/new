# Spring Boot 企业级管理平台 技术设计文档（Harness）

> **关联需求**：[需求文档](../01-product-specs/spring-boot-admin-platform-spec.md)
> **Antigravity 设计文档**：[.antigravity/specs/spring-boot-admin-platform/design.md](../../.antigravity/specs/spring-boot-admin-platform/design.md)
> **文档状态**：已确认
> **创建时间**：2025-01-01
> **最后更新**：2025-01-01
> **负责人**：@team

---

## 概述

基于 Spring Boot 2.2.x 的企业级 Web 管理平台，采用 Apache Shiro 实现 RBAC 权限体系，MyBatis 3.5.x 作为持久层，Thymeleaf 3.0.x + Bootstrap 3.3.7 作为视图层，支持 SQLite/MySQL 双数据库 Profile 切换。遵循四层架构约束（controller  service  domain  repository），基础包路径 `com.md.basePlatform`。

---

## 技术选型

| 类别 | 技术 | 版本 | 说明 |
|------|------|------|------|
| 核心框架 | Spring Boot | 2.2.13.RELEASE | 2.2.x 最终维护版 |
| 安全框架 | Apache Shiro | 1.7.1 | RBAC + Session 管理 |
| 持久层 | MyBatis | 3.5.6（via starter 2.1.4） | Mapper 接口 + XML |
| 数据源 | Alibaba Druid | 1.2.8 | 连接池 + 内置监控 |
| 视图层 | Thymeleaf | 3.0.x（Boot 管理） | 服务端渲染 |
| UI 框架 | Bootstrap | 3.3.7 | 响应式布局 |
| 调度 | Quartz | 2.3.2 | 定时任务 |
| 监控 | oshi-core | 5.8.2 | CPU/内存/磁盘采集 |
| 参数校验 | Hibernate Validator | 6.0.x（Boot 管理） | JSR-380 |
| 数据库 | SQLite 3.x / MySQL 5.7+ |  | Profile 切换 |

---

## 架构设计

### 四层架构

```
controller 层（表现层）
     调用 Service 接口
service 层（业务逻辑层）
     调用 Repository 接口 / 使用 Domain 对象
domain 层（领域模型）    repository 层（数据访问层）
                              
                         数据库（SQLite / MySQL）
```

**辅助包**：`config`、`common`、`exception`、`interceptor`、`annotation`、`aspect`

### 安全架构

Shiro 过滤器链：`anon`（静态资源、登录页） `user`（业务路径） `logout`（退出）

认证流程：`UsernamePasswordToken`  `UserRealm.doGetAuthenticationInfo`  `HashedCredentialsMatcher`（MD5+盐）

授权流程：`@RequiresPermissions`  `UserRealm.doGetAuthorizationInfo`  权限标识集合（如 `system:user:add`）

---

## 模块清单

| 模块 | Controller 路径前缀 | 核心 Service 接口 |
|------|-------------------|-----------------|
| 认证 | `/login`, `/logout` | `IUserService` |
| 用户管理 | `/system/user` | `IUserService` |
| 部门管理 | `/system/dept` | `IDeptService` |
| 岗位管理 | `/system/post` | `IPostService` |
| 菜单管理 | `/system/menu` | `IMenuService` |
| 角色管理 | `/system/role` | `IRoleService` |
| 字典管理 | `/system/dict` | `IDictTypeService`, `IDictDataService` |
| 参数管理 | `/system/config` | `IConfigService` |
| 通知公告 | `/system/notice` | `INoticeService` |
| 操作日志 | `/monitor/operlog` | `IOperLogService` |
| 在线用户 | `/monitor/online` | `IOnlineService` |
| 定时任务 | `/monitor/job` | `IJobService` |
| 服务监控 | `/monitor/server` | `IServerService` |
| 缓存监控 | `/monitor/cache` |  |
| 连接池监视 | `/druid/*` | Druid 内置 |
| 无人机管理 | `/drone/info` | `IDroneService`, `IAiService` |
| 代码生成器 | `/tool/gen` | `IGenService` |

---

## 数据库 Profile 切换

| Profile | 数据源 | 适用场景 |
|---------|--------|---------|
| `sqlite` | SQLite 文件（`./data/basePlatform.db`） | 开发/测试 |
| `mysql` | MySQL 5.7+（Druid 连接池） | 生产 |

切换命令：`--spring.profiles.active=mysql`

**SQLite 限制**：不支持 `FIND_IN_SET`（数据权限需改写）、写操作串行化、Quartz 使用 `RAMJobStore`。

---

## 正确性属性摘要

| 属性编号 | 属性名称 | 验证需求 |
|----------|----------|---------|
| 属性 1 | 密码存储安全性（MD5+盐不可逆） | 需求 1.7 |
| 属性 2 | 查询过滤结果一致性 | 需求 3.3、17.3 |
| 属性 3 | 字典缓存一致性（Round-Trip） | 需求 8.6 |
| 属性 4 | 操作日志完整性 | 需求 11.1、11.2 |
| 属性 5 | 统一响应格式不变量 | 需求 18.1-18.5 |
| 属性 6 | 代码生成架构合规性 | 需求 21.1、21.3 |

属性测试使用 jqwik 1.6.x，每个属性最少运行 100 次迭代。

---

## 主要风险

| 风险 | 影响 | 应对 |
|------|------|------|
| Spring Boot 2.2.x 已停止维护 | 高 | 使用 2.2.13.RELEASE，规划升级 |
| SQLite 不支持 FIND_IN_SET | 高 | SQLite Profile 下用 Java 代码实现树遍历 |
| Shiro 自动配置冲突 | 中 | 禁用自动配置，完全手动配置 |
| AI 服务不稳定 | 中 | 超时降级，允许手动填写 |
| Quartz SQLite 持久化 | 中 | SQLite 用 RAMJobStore |

---

## 变更记录

| 版本 | 日期 | 变更内容 | 变更人 |
|------|------|----------|--------|
| v1.0 | 2025-01-01 | 初始版本 | @team |
