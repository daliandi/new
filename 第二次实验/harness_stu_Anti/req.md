项目开发需求具体如下：
1 技术栈
（1）系统环境
      Java EE 8
      Servlet 3.0
      Apache Maven 3
（2）主框架
      Spring Boot 2.2.x
      Spring Framework 5.2.x
      Apache Shiro 1.7
（3）持久层
      Apache MyBatis 3.5.x
      Hibernate Validation 6.0.x
      Alibaba Druid 1.2.x
（4）视图层
      Bootstrap 3.3.7
      Thymeleaf 3.0.x
2 支持SQLite，MySQL数据库，并可以自由切换；
3 业务功能：
   无人机信息管理，包括无人机信息的录入，查询，删除和修改，无人机的属性由AI自动生成；
4 基础功能设计上参考“RuoYi”系统；
5 服务层，数据操作层设计上接口和实现分别放在不同包；
6 拦截器：建立拦截器单独的包，将各类拦截器放到该包，执行时打印请求信息；
7 设计上尽量采用注解，减少对xml配置文件的依赖；
8 根据上面要求，还有遗漏的请补充。



MySQL 数据库初始化 由于我们确定已有可用 Docker MySQL 集群，请将本项目自带 uav-management/docs/init-mysql.sql 文件贴入您的 Docker MySQL 客户端执行（这会快速为您建立 uav_db 并插入演示无人机数据）。

启动 Spring Boot 服务 在 uav-management 目录下执行启动：

bash
mvn spring-boot:run
测试账号（定义在 UavUserRealm 内）：

账号：admin
密码：admin123
进入管理后台 浏览器访问：http://localhost:8080 （自动跳转登录页）。可体验列表的分页，Bootstrap 加持下的后台界面、以及基于 AI 随机推理的属性自动生成。

项目已全部跑通 Antigravity Harness 开发流与六合门禁。如果有其他的业务需求（例如扩充拦截器、添加多租户）亦或需部署，请随时告知。