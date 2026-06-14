# 需求文档

## 简介

本项目为基于 Spring Boot 2.2.x 的企业级 Web 管理平台，参考 RuoYi 系统设计，提供完整的权限管理、组织架构、系统监控等基础功能，以及无人机信息管理业务功能。系统采用四层架构（controller → service → domain ← repository），支持 SQLite 与 MySQL 双数据库切换，视图层使用 Thymeleaf + Bootstrap，后期可迁移至 Vue。

---

## 词汇表

- **System**：Spring Boot 管理平台整体系统
- **AuthModule**：认证与授权模块（基于 Apache Shiro）
- **UserModule**：用户管理模块
- **DeptModule**：部门管理模块
- **PostModule**：岗位管理模块
- **MenuModule**：菜单管理模块
- **RoleModule**：角色管理模块
- **DictModule**：字典管理模块
- **ParamModule**：参数管理模块
- **NoticeModule**：通知公告模块
- **LogModule**：操作日志模块
- **OnlineModule**：在线用户模块
- **JobModule**：定时任务模块
- **MonitorModule**：服务监控模块
- **CacheModule**：缓存监控模块
- **DruidModule**：连接池监视模块
- **DroneModule**：无人机信息管理模块
- **AIService**：AI 属性生成服务（外部或内置）
- **Interceptor**：拦截器，负责打印请求信息
- **DataScope**：数据权限范围（全部/本部门/本部门及子部门/仅本人）
- **SessionManager**：会话管理器，基于 Shiro Session
- **GlobalExceptionHandler**：全局异常处理器
- **ResponseWrapper**：统一响应格式包装器
- **CodeGenerator**：代码生成器

---

## 需求

### 需求 1：用户登录与认证

**用户故事：** 作为系统用户，我希望通过用户名和密码登录系统，以便安全访问受保护的功能。

#### 验收标准

1. WHEN 用户提交正确的用户名和密码，THE AuthModule SHALL 验证凭据并创建 Shiro Session，跳转至系统首页
2. WHEN 用户提交错误的用户名或密码，THE AuthModule SHALL 返回"用户名或密码错误"提示，并记录失败次数
3. WHEN 同一账号连续登录失败次数达到 5 次，THE AuthModule SHALL 锁定该账号 30 分钟并返回锁定提示
4. WHEN 用户请求受保护资源且未登录，THE AuthModule SHALL 重定向至登录页面
5. WHEN 用户成功登录，THE LogModule SHALL 记录登录时间、IP 地址和浏览器信息
6. WHEN 用户点击退出登录，THE AuthModule SHALL 销毁 Shiro Session 并重定向至登录页面
7. THE AuthModule SHALL 对用户密码使用 MD5 加盐哈希存储，禁止明文保存
8. WHERE 启用验证码配置，THE AuthModule SHALL 在登录表单中展示图形验证码并校验其正确性

---

### 需求 2：会话管理

**用户故事：** 作为系统管理员，我希望系统能管理用户会话，以便控制并发登录和超时行为。

#### 验收标准

1. WHILE 用户已登录且持续操作，THE SessionManager SHALL 维持 Session 有效，默认超时时间为 30 分钟
2. WHEN 用户 Session 超时，THE SessionManager SHALL 使 Session 失效并在下次请求时重定向至登录页
3. WHERE 配置了单点登录限制，THE SessionManager SHALL 在同一账号新登录时踢出旧 Session
4. THE SessionManager SHALL 将活跃 Session 信息存储，供 OnlineModule 查询展示

---

### 需求 3：用户管理

**用户故事：** 作为系统管理员，我希望管理系统用户，以便维护操作人员账号信息。

#### 验收标准

1. THE UserModule SHALL 支持新增用户，必填字段包括用户名、姓名、所属部门、所属岗位、角色
2. WHEN 新增用户时用户名已存在，THE UserModule SHALL 返回"用户名已存在"错误提示
3. THE UserModule SHALL 支持按用户名、姓名、部门、状态进行分页查询，每页默认 10 条
4. THE UserModule SHALL 支持修改用户基本信息，包括姓名、手机号、邮箱、部门、岗位、角色、状态
5. WHEN 管理员重置用户密码，THE UserModule SHALL 将密码重置为系统默认密码并以 MD5 加盐哈希存储
6. WHEN 管理员禁用用户，THE UserModule SHALL 将用户状态置为禁用，该用户下次请求时 THE AuthModule SHALL 拒绝登录
7. THE UserModule SHALL 支持删除用户，不允许删除当前登录用户自身
8. THE UserModule SHALL 支持批量导入用户（Excel 格式），导入失败时返回失败行详情
9. THE UserModule SHALL 支持导出用户列表为 Excel 文件
10. WHERE 启用数据权限，THE UserModule SHALL 仅展示当前用户 DataScope 范围内的用户数据

---

### 需求 4：部门管理

**用户故事：** 作为系统管理员，我希望维护组织架构，以便按树形结构管理公司、部门和小组。

#### 验收标准

1. THE DeptModule SHALL 以树形结构展示所有部门节点，支持展开和折叠
2. THE DeptModule SHALL 支持新增部门，必填字段包括部门名称、上级部门、排序号、状态
3. WHEN 新增部门时部门名称在同级下已存在，THE DeptModule SHALL 返回"部门名称已存在"错误提示
4. THE DeptModule SHALL 支持修改部门信息，不允许将部门的上级设置为其自身或其子孙节点
5. WHEN 删除部门时该部门下存在子部门或用户，THE DeptModule SHALL 拒绝删除并返回提示信息
6. THE DeptModule SHALL 支持按部门名称和状态进行筛选查询

---

### 需求 5：岗位管理

**用户故事：** 作为系统管理员，我希望配置岗位信息，以便为用户分配职务。

#### 验收标准

1. THE PostModule SHALL 支持新增岗位，必填字段包括岗位编码、岗位名称、排序号、状态
2. WHEN 新增岗位时岗位编码已存在，THE PostModule SHALL 返回"岗位编码已存在"错误提示
3. THE PostModule SHALL 支持分页查询岗位列表，支持按岗位名称和状态筛选
4. THE PostModule SHALL 支持修改岗位信息
5. WHEN 删除岗位时该岗位下存在关联用户，THE PostModule SHALL 拒绝删除并返回提示信息

---

### 需求 6：菜单管理

**用户故事：** 作为系统管理员，我希望配置系统菜单和权限标识，以便控制用户可访问的功能。

#### 验收标准

1. THE MenuModule SHALL 以树形结构展示菜单，菜单类型分为目录、菜单、按钮三种
2. THE MenuModule SHALL 支持新增菜单，必填字段包括菜单名称、上级菜单、菜单类型、排序号
3. WHEN 菜单类型为菜单时，THE MenuModule SHALL 要求填写路由地址和组件路径
4. WHEN 菜单类型为按钮时，THE MenuModule SHALL 要求填写权限标识（如 system:user:add）
5. THE MenuModule SHALL 支持修改和删除菜单
6. WHEN 删除菜单时该菜单下存在子菜单，THE MenuModule SHALL 拒绝删除并返回提示信息
7. THE AuthModule SHALL 根据当前用户角色动态加载其有权限的菜单树

---

### 需求 7：角色管理

**用户故事：** 作为系统管理员，我希望配置角色及其权限，以便按角色控制用户的功能和数据访问范围。

#### 验收标准

1. THE RoleModule SHALL 支持新增角色，必填字段包括角色名称、角色编码、排序号、状态
2. THE RoleModule SHALL 支持为角色分配菜单权限（含按钮权限）
3. THE RoleModule SHALL 支持为角色设置数据权限范围，可选值为：全部数据、本部门数据、本部门及子部门数据、仅本人数据、自定义部门数据
4. THE RoleModule SHALL 支持分页查询角色列表，支持按角色名称和状态筛选
5. THE RoleModule SHALL 支持修改角色信息和权限分配
6. WHEN 删除角色时该角色下存在关联用户，THE RoleModule SHALL 拒绝删除并返回提示信息
7. THE RoleModule SHALL 支持批量授权用户至指定角色

---

### 需求 8：字典管理

**用户故事：** 作为系统管理员，我希望维护系统字典数据，以便统一管理下拉选项等固定数据。

#### 验收标准

1. THE DictModule SHALL 支持新增字典类型，必填字段包括字典名称、字典类型编码、状态
2. THE DictModule SHALL 支持为字典类型新增字典数据项，必填字段包括数据标签、数据键值、排序号
3. THE DictModule SHALL 支持分页查询字典类型列表，支持按字典名称和状态筛选
4. THE DictModule SHALL 支持修改和删除字典类型及字典数据项
5. WHEN 删除字典类型时，THE DictModule SHALL 同步删除该类型下的所有字典数据项
6. THE System SHALL 将字典数据缓存至内存，WHEN 字典数据变更时 THE DictModule SHALL 刷新对应缓存

---

### 需求 9：参数管理

**用户故事：** 作为系统管理员，我希望动态配置系统参数，以便在不重启服务的情况下调整系统行为。

#### 验收标准

1. THE ParamModule SHALL 支持新增参数，必填字段包括参数名称、参数键名、参数键值、参数类型（系统内置/用户自定义）
2. WHEN 新增参数时参数键名已存在，THE ParamModule SHALL 返回"参数键名已存在"错误提示
3. THE ParamModule SHALL 支持分页查询参数列表，支持按参数名称、键名和类型筛选
4. THE ParamModule SHALL 支持修改用户自定义参数，系统内置参数不允许删除
5. THE System SHALL 将参数数据缓存至内存，WHEN 参数变更时 THE ParamModule SHALL 刷新对应缓存

---

### 需求 10：通知公告

**用户故事：** 作为系统管理员，我希望发布通知公告，以便向系统用户传达重要信息。

#### 验收标准

1. THE NoticeModule SHALL 支持新增公告，必填字段包括公告标题、公告类型（通知/公告）、公告内容、状态
2. THE NoticeModule SHALL 支持分页查询公告列表，支持按公告标题和操作人筛选
3. THE NoticeModule SHALL 支持修改和删除公告
4. WHEN 用户登录后，THE NoticeModule SHALL 在首页展示最新的已发布公告列表

---

### 需求 11：操作日志

**用户故事：** 作为系统管理员，我希望查看系统操作日志，以便审计用户行为和排查异常。

#### 验收标准

1. WHEN 用户执行标注了 @Log 注解的操作，THE LogModule SHALL 异步记录操作人、操作模块、操作类型、请求 URL、请求方法、请求参数、响应结果、操作时间、IP 地址
2. WHEN 系统发生未捕获异常，THE LogModule SHALL 记录异常信息、堆栈摘要、请求 URL 和操作人
3. THE LogModule SHALL 支持分页查询操作日志，支持按操作人、操作模块、操作类型、时间范围筛选
4. THE LogModule SHALL 支持分页查询异常日志，支持按操作人、时间范围筛选
5. THE LogModule SHALL 支持删除操作日志和异常日志
6. THE LogModule SHALL 支持导出日志列表为 Excel 文件

---

### 需求 12：在线用户监控

**用户故事：** 作为系统管理员，我希望查看当前在线用户，以便监控系统活跃状态。

#### 验收标准

1. THE OnlineModule SHALL 展示当前所有活跃 Session 的用户信息，包括用户名、登录 IP、登录时间、浏览器、操作系统
2. THE OnlineModule SHALL 支持按用户名和登录 IP 筛选在线用户
3. WHEN 管理员强制下线某用户，THE OnlineModule SHALL 销毁该用户的 Session，该用户下次请求时被重定向至登录页

---

### 需求 13：定时任务

**用户故事：** 作为系统管理员，我希望在线管理定时任务，以便调度后台自动化作业。

#### 验收标准

1. THE JobModule SHALL 支持新增定时任务，必填字段包括任务名称、任务组名、调用目标（Bean 名称.方法名）、Cron 表达式、执行策略、状态
2. THE JobModule SHALL 支持修改、删除、暂停、恢复和立即执行定时任务
3. WHEN 定时任务执行完成，THE JobModule SHALL 记录执行日志，包括任务名称、执行时间、耗时、执行状态和异常信息
4. THE JobModule SHALL 支持分页查询任务执行日志，支持按任务名称、任务组和执行状态筛选
5. IF 定时任务执行抛出异常，THEN THE JobModule SHALL 将执行状态标记为失败并记录异常信息，不影响其他任务执行
6. THE JobModule SHALL 支持 Cron 表达式在线校验，WHEN 表达式格式非法时返回错误提示

---

### 需求 14：服务监控

**用户故事：** 作为系统管理员，我希望查看服务器运行状态，以便掌握系统资源使用情况。

#### 验收标准

1. THE MonitorModule SHALL 展示服务器 CPU 使用率、核心数和型号信息
2. THE MonitorModule SHALL 展示服务器内存总量、已用量、剩余量和使用率
3. THE MonitorModule SHALL 展示 JVM 堆内存总量、已用量、最大量、JVM 版本和启动时间
4. THE MonitorModule SHALL 展示磁盘分区列表，包括盘符、总量、已用量、剩余量和使用率
5. THE MonitorModule SHALL 展示操作系统名称、架构和服务器 IP 地址

---

### 需求 15：缓存监控

**用户故事：** 作为系统管理员，我希望查看和管理系统缓存，以便维护缓存数据的有效性。

#### 验收标准

1. THE CacheModule SHALL 展示当前所有缓存名称列表及各缓存的键数量
2. THE CacheModule SHALL 支持查看指定缓存名称下的所有缓存键列表
3. THE CacheModule SHALL 支持查看指定缓存键对应的缓存值内容
4. THE CacheModule SHALL 支持清除指定缓存名称下的所有缓存数据
5. THE CacheModule SHALL 支持一键清除全部缓存数据

---

### 需求 16：连接池监视

**用户故事：** 作为系统管理员，我希望监视数据库连接池状态，以便分析 SQL 性能瓶颈。

#### 验收标准

1. THE DruidModule SHALL 通过 Druid 内置监控页面展示数据源连接池状态，包括活跃连接数、等待线程数、执行 SQL 数
2. THE DruidModule SHALL 展示 SQL 执行统计信息，包括执行次数、平均耗时、最大耗时、最慢 SQL
3. THE DruidModule SHALL 支持按执行次数和耗时排序 SQL 列表，辅助定位性能瓶颈
4. WHERE 启用 Druid 监控登录保护，THE DruidModule SHALL 要求输入监控用户名和密码方可访问

---

### 需求 17：无人机信息管理

**用户故事：** 作为业务操作员，我希望管理无人机信息，以便维护无人机资产台账。

#### 验收标准

1. THE DroneModule SHALL 支持新增无人机记录，操作员填写无人机编号后，THE AIService SHALL 自动生成无人机属性（型号、制造商、最大载重、最大飞行时间、最大飞行速度、最大飞行高度、用途分类）
2. WHEN AIService 调用失败，THE DroneModule SHALL 允许操作员手动填写无人机属性，并返回 AI 生成失败的提示
3. THE DroneModule SHALL 支持分页查询无人机列表，支持按无人机编号、型号、用途分类和状态筛选，每页默认 10 条
4. THE DroneModule SHALL 支持修改无人机信息，修改时可选择重新触发 AI 属性生成
5. THE DroneModule SHALL 支持删除无人机记录，支持批量删除
6. THE DroneModule SHALL 支持导出无人机列表为 Excel 文件
7. WHERE 启用数据权限，THE DroneModule SHALL 仅展示当前用户 DataScope 范围内的无人机数据

---

### 需求 18：统一异常处理

**用户故事：** 作为开发者，我希望系统统一处理所有异常，以便向前端返回一致的错误响应格式。

#### 验收标准

1. THE GlobalExceptionHandler SHALL 捕获业务异常（ServiceException）并返回对应的错误码和错误消息
2. THE GlobalExceptionHandler SHALL 捕获参数校验异常（MethodArgumentNotValidException）并返回字段级错误详情
3. THE GlobalExceptionHandler SHALL 捕获权限不足异常（UnauthorizedException）并返回 HTTP 403 状态码和提示信息
4. THE GlobalExceptionHandler SHALL 捕获所有未预期异常并返回通用错误提示，同时将异常详情记录至日志，不向前端暴露堆栈信息
5. THE ResponseWrapper SHALL 统一所有接口响应格式为 `{"code": int, "msg": string, "data": object}`

---

### 需求 19：请求拦截器

**用户故事：** 作为开发者，我希望系统通过拦截器打印请求信息，以便调试和审计 HTTP 请求。

#### 验收标准

1. THE Interceptor SHALL 在每次 HTTP 请求进入时打印请求 URL、HTTP 方法、客户端 IP、请求参数和当前用户名
2. THE Interceptor SHALL 在每次 HTTP 请求完成后打印响应状态码和请求处理耗时（毫秒）
3. THE Interceptor SHALL 放行静态资源请求（/static/**、/css/**、/js/**、/images/**），不打印静态资源请求日志
4. THE System SHALL 将所有拦截器类放置在独立的 interceptor 包下

---

### 需求 20：数据库多源切换

**用户故事：** 作为运维人员，我希望系统支持 SQLite 和 MySQL 数据库切换，以便在不同环境下灵活部署。

#### 验收标准

1. THE System SHALL 通过 Spring Profile（`sqlite` / `mysql`）切换数据源配置，无需修改业务代码
2. WHEN 激活 `sqlite` Profile，THE System SHALL 使用 SQLite 数据库文件作为数据源
3. WHEN 激活 `mysql` Profile，THE System SHALL 使用 MySQL 数据库连接池（Druid）作为数据源
4. THE System SHALL 提供 SQLite 和 MySQL 各自的 DDL 初始化脚本，确保表结构一致

---

### 需求 21：代码生成器

**用户故事：** 作为开发者，我希望系统提供代码生成功能，以便快速生成标准的 CRUD 代码骨架。

#### 验收标准

1. THE CodeGenerator SHALL 读取数据库表结构，生成对应的 domain 实体类、Mapper 接口、Mapper XML、Service 接口、Service 实现、Controller 和 Thymeleaf 页面模板
2. THE CodeGenerator SHALL 支持配置生成代码的包路径、作者、模块名称
3. THE CodeGenerator SHALL 生成的代码符合四层架构约束，service 接口与实现分别放在 service 和 service.impl 包下
4. THE CodeGenerator SHALL 支持预览生成代码内容，确认后再下载 ZIP 压缩包

---

### 需求 22：文件上传

**用户故事：** 作为系统用户，我希望系统支持文件上传，以便上传头像、导入数据等。

#### 验收标准

1. THE System SHALL 支持单文件上传，默认限制单文件大小不超过 10MB
2. WHEN 上传文件超过大小限制，THE System SHALL 返回"文件大小超出限制"错误提示
3. THE System SHALL 对上传文件进行扩展名白名单校验，WHEN 文件扩展名不在白名单内时 THE System SHALL 拒绝上传并返回提示
4. THE System SHALL 将上传文件存储至配置的本地目录，并返回可访问的相对路径

---

### 需求 23：跨域配置

**用户故事：** 作为前端开发者，我希望系统支持跨域请求配置，以便前后端分离场景下正常调用接口。

#### 验收标准

1. WHERE 启用跨域配置，THE System SHALL 通过 Spring MVC CorsFilter 统一处理跨域请求
2. THE System SHALL 支持通过配置文件指定允许的跨域来源、请求方法和请求头
3. WHEN 收到 OPTIONS 预检请求，THE System SHALL 返回 HTTP 200 状态码和正确的 CORS 响应头

---

### 需求 24：服务层与数据层接口分离

**用户故事：** 作为开发者，我希望 service 和 repository 的接口与实现分包存放，以便提高代码可维护性和可测试性。

#### 验收标准

1. THE System SHALL 将 service 接口定义放置在 `service` 包下，将 service 实现类放置在 `service.impl` 包下
2. THE System SHALL 将 repository（Mapper）接口定义放置在 `repository` 包下
3. THE System SHALL 通过 Spring 依赖注入 service 接口，controller 不直接依赖 service 实现类
4. THE System SHALL 遵循四层架构约束：controller 仅调用 service，service 仅调用 repository，domain 为纯 POJO
