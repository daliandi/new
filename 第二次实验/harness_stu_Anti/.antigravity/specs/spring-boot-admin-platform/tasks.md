# 实现任务列表

## 任务概览

基于需求文档（24 个需求）和技术设计文档，按实现依赖顺序分为 11 个任务组。

---

## 任务列表

- [ ] 1. 项目基础搭建
  - [ ] 1.1 更新 pom.xml：Spring Boot 2.2.x，添加 Shiro/MyBatis/Druid/Thymeleaf/Quartz/oshi/POI/SQLite/MySQL/Validation/Lombok 依赖
  - [ ] 1.2 创建完整包结构及各包 package-info.java
  - [ ] 1.3 创建 application.yml、application-sqlite.yml、application-mysql.yml
  - [ ] 1.4 创建 sql/schema-mysql.sql（所有表 MySQL DDL）
  - [ ] 1.5 创建 sql/schema-sqlite.sql（所有表 SQLite DDL）
  - [ ] 1.6 创建 sql/data.sql（初始化数据：管理员账号、默认角色、菜单树、字典、系统参数）

- [ ] 2. 公共基础设施
  - [ ] 2.1 创建 common/constant/Constants.java、UserConstants.java、CacheConstants.java
  - [ ] 2.2 创建 common/enums/BusinessType.java、DataScopeType.java、MenuType.java
  - [ ] 2.3 创建 common/utils/Md5Utils.java、SecurityUtils.java、IpUtils.java、DateUtils.java、StringUtils.java、PageUtils.java
  - [ ] 2.4 创建 common/utils/ExcelUtils.java（Apache POI 导入导出）
  - [ ] 2.5 创建 domain/dto/AjaxResult.java、TableDataInfo.java
  - [ ] 2.6 创建 exception/ServiceException.java、FileUploadException.java、GlobalExceptionHandler.java
  - [ ] 2.7 创建 annotation/Log.java（操作日志注解）
  - [ ] 2.8 创建 aspect/LogAspect.java（AOP 切面，异步写入操作日志）
  - [ ] 2.9 创建 interceptor/RequestLoggingInterceptor.java（打印请求信息，放行静态资源）

- [ ] 3. 配置层
  - [ ] 3.1 创建 config/DruidConfig.java（Druid 数据源，sqlite/mysql Profile 切换，StatViewServlet）
  - [ ] 3.2 创建 config/MyBatisConfig.java（@MapperScan，PageHelper 分页插件）
  - [ ] 3.3 创建 config/ShiroConfig.java（ShiroFilterFactoryBean、SecurityManager、自定义 Realm、Session 管理）
  - [ ] 3.4 创建 config/CacheConfig.java（ConcurrentHashMap Spring Cache）
  - [ ] 3.5 创建 config/QuartzConfig.java（SchedulerFactoryBean）
  - [ ] 3.6 创建 config/WebMvcConfig.java（注册拦截器，静态资源映射）
  - [ ] 3.7 创建 config/CorsConfig.java（CorsFilter 跨域）
  - [ ] 3.8 创建 config/FileUploadConfig.java（MultipartResolver，10MB 限制）

- [ ] 4. 认证与安全模块
  - [ ] 4.1 创建 domain/SysUser.java（纯 POJO）
  - [ ] 4.2 创建 repository/SysUserMapper.java + mapper/SysUserMapper.xml（含数据权限 SQL 片段）
  - [ ] 4.3 创建 config/shiro/UserRealm.java（AuthorizingRealm，认证+授权）
  - [ ] 4.4 创建 service/IUserService.java（认证相关方法）+ service/impl/UserServiceImpl.java（认证部分）
  - [ ] 4.5 创建 domain/dto/LoginBody.java（登录请求 DTO）
  - [ ] 4.6 创建 controller/LoginController.java（GET/POST /login、GET /logout、GET /captchaImage）
  - [ ] 4.7 创建 templates/login.html（Bootstrap 3.3.7 登录页，含验证码）
  - [ ] 4.8 创建 controller/IndexController.java（首页，加载菜单树和公告）
  - [ ] 4.9 创建 templates/index.html + templates/main.html（系统主框架页）

- [ ] 5. 系统基础模块（用户/部门/岗位/菜单/角色）
  - [ ] 5.1 创建 domain/SysDept.java、SysPost.java、SysMenu.java、SysRole.java（纯 POJO）
  - [ ] 5.2 创建 repository/SysDeptMapper.java、SysPostMapper.java、SysMenuMapper.java、SysRoleMapper.java 及对应 XML
  - [ ] 5.3 创建 repository/SysUserRoleMapper.java、SysUserPostMapper.java、SysRoleMenuMapper.java、SysRoleDeptMapper.java
  - [ ] 5.4 创建 service/IDeptService.java + service/impl/DeptServiceImpl.java（树形结构、循环引用检查）
  - [ ] 5.5 创建 service/IPostService.java + service/impl/PostServiceImpl.java
  - [ ] 5.6 创建 service/IMenuService.java + service/impl/MenuServiceImpl.java（动态菜单树、权限标识集合）
  - [ ] 5.7 创建 service/IRoleService.java + service/impl/RoleServiceImpl.java（权限分配、数据权限）
  - [ ] 5.8 完善 service/IUserService.java + service/impl/UserServiceImpl.java（CRUD、导入导出、数据权限过滤）
  - [ ] 5.9 创建 controller/system/UserController.java（@RequiresPermissions、@Log 注解）
  - [ ] 5.10 创建 controller/system/DeptController.java、PostController.java、MenuController.java、RoleController.java
  - [ ] 5.11 创建 templates/system/user/list.html、dept/list.html、post/list.html、menu/list.html、role/list.html

- [ ] 6. 系统配置模块（字典/参数/通知公告）
  - [ ] 6.1 创建 domain/SysDictType.java、SysDictData.java、SysConfig.java、SysNotice.java（纯 POJO）
  - [ ] 6.2 创建 repository/SysDictTypeMapper.java、SysDictDataMapper.java、SysConfigMapper.java、SysNoticeMapper.java
  - [ ] 6.3 创建 service/IDictTypeService.java + service/impl/DictTypeServiceImpl.java（含缓存刷新）
  - [ ] 6.4 创建 service/IDictDataService.java + service/impl/DictDataServiceImpl.java
  - [ ] 6.5 创建 service/IConfigService.java + service/impl/ConfigServiceImpl.java（含缓存刷新）
  - [ ] 6.6 创建 service/INoticeService.java + service/impl/NoticeServiceImpl.java
  - [ ] 6.7 创建 controller/system/DictTypeController.java、DictDataController.java、ConfigController.java、NoticeController.java
  - [ ] 6.8 创建 templates/system/dict/list.html、config/list.html、notice/list.html

- [ ] 7. 日志与监控模块
  - [ ] 7.1 创建 domain/SysOperLog.java（纯 POJO）
  - [ ] 7.2 创建 repository/SysOperLogMapper.java + mapper/SysOperLogMapper.xml
  - [ ] 7.3 创建 service/IOperLogService.java + service/impl/OperLogServiceImpl.java（异步保存、分页查询、导出）
  - [ ] 7.4 完善 aspect/LogAspect.java（调用 IOperLogService @Async 异步写入）
  - [ ] 7.5 创建 service/IOnlineService.java + service/impl/OnlineServiceImpl.java（从 Shiro SessionManager 获取活跃 Session）
  - [ ] 7.6 创建 domain/dto/ServerInfo.java（CPU/内存/磁盘/JVM DTO）
  - [ ] 7.7 创建 service/impl/ServerServiceImpl.java（使用 oshi 采集系统信息）
  - [ ] 7.8 创建 controller/monitor/OperLogController.java、OnlineController.java、ServerController.java、CacheController.java
  - [ ] 7.9 创建 templates/monitor/online/list.html、server/index.html、cache/index.html、operlog/list.html

- [ ] 8. 定时任务模块
  - [ ] 8.1 创建 domain/SysJob.java、SysJobLog.java（纯 POJO）
  - [ ] 8.2 创建 repository/SysJobMapper.java + mapper/SysJobMapper.xml、SysJobLogMapper.java
  - [ ] 8.3 创建 quartz/AbstractQuartzJob.java（抽象任务基类，记录执行日志）
  - [ ] 8.4 创建 quartz/QuartzJobExecution.java（并发任务）、QuartzDisallowConcurrentExecution.java（禁并发任务）
  - [ ] 8.5 创建 service/IJobService.java + service/impl/JobServiceImpl.java（增删改查、暂停/恢复/立即执行、Cron 校验）
  - [ ] 8.6 创建 service/IJobLogService.java + service/impl/JobLogServiceImpl.java
  - [ ] 8.7 创建 controller/monitor/JobController.java、JobLogController.java
  - [ ] 8.8 创建 templates/monitor/job/list.html、jobLog/list.html

- [ ] 9. 无人机业务模块
  - [ ] 9.1 创建 domain/DroneInfo.java（纯 POJO，含所有 AI 生成属性字段）
  - [ ] 9.2 创建 repository/DroneInfoMapper.java + mapper/DroneInfoMapper.xml（含数据权限 SQL 片段）
  - [ ] 9.3 创建 service/IAiService.java（AI 属性生成接口）+ service/impl/AiServiceImpl.java（调用 AI 接口，超时降级处理）
  - [ ] 9.4 创建 service/IDroneService.java + service/impl/DroneServiceImpl.java（CRUD、AI 触发、数据权限过滤、导出）
  - [ ] 9.5 创建 controller/drone/DroneController.java（@RequiresPermissions、@Log 注解，含 AI 生成触发接口）
  - [ ] 9.6 创建 templates/drone/list.html（Bootstrap 3.3.7，含 AI 生成按钮和手动填写降级）

- [ ] 10. 代码生成器模块
  - [ ] 10.1 创建 domain/GenTable.java、GenTableColumn.java（代码生成配置 POJO）
  - [ ] 10.2 创建 repository/GenTableMapper.java、GenTableColumnMapper.java（读取数据库表结构）
  - [ ] 10.3 创建 service/IGenService.java + service/impl/GenServiceImpl.java（读表结构、渲染模板、打包 ZIP）
  - [ ] 10.4 创建 resources/vm/ 目录下 Velocity 模板（entity.java.vm、mapper.java.vm、mapper.xml.vm、service.java.vm、serviceImpl.java.vm、controller.java.vm、list.html.vm）
  - [ ] 10.5 创建 controller/tool/GenController.java（预览、下载 ZIP）
  - [ ] 10.6 创建 templates/tool/gen/list.html

- [ ] 11. 单元测试
  - [ ] 11.1 创建 UserServiceImplTest.java（@ExtendWith(MockitoExtension.class)，覆盖：新增用户、用户名重复、删除自身账号拦截、重置密码）
  - [ ] 11.2 创建 DeptServiceImplTest.java（覆盖：树形构建、循环引用检查、删除前置校验）
  - [ ] 11.3 创建 RoleServiceImplTest.java（覆盖：权限分配、数据权限设置、删除前置校验）
  - [ ] 11.4 创建 DroneServiceImplTest.java（覆盖：AI 生成成功、AI 生成失败降级、分页查询、批量删除）
  - [ ] 11.5 创建 LoginControllerTest.java（@WebMvcTest，覆盖：登录成功、密码错误、账号锁定、未登录重定向）
  - [ ] 11.6 创建 UserControllerTest.java（@WebMvcTest，覆盖：分页查询、新增、修改、删除、权限拦截）
  - [ ] 11.7 创建 DroneControllerTest.java（@WebMvcTest，覆盖：CRUD 接口、AI 触发接口）
  - [ ] 11.8 创建 SysUserMapperTest.java（@MybatisTest，覆盖：按条件查询、数据权限 SQL）
  - [ ] 11.9 创建 GlobalExceptionHandlerTest.java（覆盖：ServiceException、参数校验异常、权限异常、未知异常）
  - [ ] 11.10 执行 mvn clean verify 确认测试覆盖率 ≥ 80%
