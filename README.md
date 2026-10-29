# 学生管理系统：本地微服务项目

后端使用 Java 17+、Spring Boot 3.3.5、MyBatis、MySQL 8，前端继续使用 Vue 3。无需 Docker 或 Nginx。

| 模块 | 端口 | 职责 |
| --- | --- | --- |
| gateway-service | 8080 | 统一入口，按路径转发请求、Authorization、查询参数和响应状态 |
| identity-service | 8081 | 登录、用户信息、学生、教师及关联账号 |
| academic-service | 8082 | 班级、课程、选课、成绩 |
| reporting-service | 8083 | 通过 HTTP 聚合两个业务服务的统计，不直接访问数据库 |
| service-common | 无 | 统一响应、实体、JWT、Web 配置 |

## 本地启动

1. 安装 JDK 17+、Maven 3.9+、MySQL 8 和 Node.js。
2. 在本地 MySQL 执行 `springboot-houtai/init.sql`。脚本创建数据库和演示数据，不会迁移已有远程库。
3. 在项目根目录复制 `.env.example` 为 `.env`，配置数据库账号、密码及至少 32 字节的 JWT_SECRET。启动脚本会读取该文件，IDE 运行时请自行配置对应环境变量。
4. 执行：

```powershell
./scripts/start-services.ps1
```

脚本先执行 Maven verify，再后台启动四个 Java 进程并等待健康检查。日志保存在 `.local-services/`。已构建时可使用 `-SkipBuild`。

5. 启动前端：

```powershell
cd vue-admin
npm install
npm run dev
```

前端默认连接 `http://localhost:8080/api`，无需修改已有业务 API。停止后端使用 `./scripts/stop-services.ps1`。

## IDE 与手工运行

将 `springboot-houtai/pom.xml` 作为 Maven 多模块项目导入 IDE，分别运行四个 `*Application`。业务服务需要相同的 `JWT_SECRET`、`DB_URL`、`DB_USERNAME`、`DB_PASSWORD`。默认连接本地 `student_management_system` 库。

```powershell
mvn -f springboot-houtai/pom.xml verify
java -jar springboot-houtai/identity-service/target/identity-service-0.0.1-SNAPSHOT.jar
# 其他三个服务在独立终端中运行相应 JAR
```

可通过 IDENTITY_SERVICE_URL、ACADEMIC_SERVICE_URL、REPORTING_SERVICE_URL 配置服务地址；通过各进程的 SERVER_PORT 修改端口。自定义端口时应同步修改启动脚本的端口检查与健康检查。

## 接口与检查

- `/api/auth/**`、`/api/students/**`、`/api/teachers/**` → 人员服务。
- `/api/classes/**`、`/api/courses/**`、`/api/grades/**`、`/api/course-selections/**` → 教务服务。
- `/api/stats` → 统计服务，保持原有四项统计字段；依赖失败返回 HTTP 503。
- 网关 `/health` 检查网关进程；各服务 `/actuator/health` 检查自身健康（业务服务包含数据库检查）。
- 业务服务各自校验 JWT，网关不替代业务鉴权。跨域配置由业务服务返回。

## 当前边界

这是可以独立构建、运行的服务拆分版本，数据库暂时共享。现有 SQL 包含跨领域 JOIN 和外键级联，学生/教师操作仍会维护账号，因此将其放在同一服务；未引入跨服务事务。完全独立数据库还需改造关联查询、外键和数据同步策略。

统计服务保留原有公开统计行为。系统原有的明文密码和角色权限实现仍需在正式上线前单独改造。Node 后端 `vue-admin/backend` 保留作为旧实现，不参与当前微服务启动。

演示账号：`admin`、`teacher1`、`student1`，密码均为 `admin123`。