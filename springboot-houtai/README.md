# 学生信息管理系统 - Spring Boot 后端

## 项目结构

```
springboot-houtai/
├── src/main/java/com/binlicoder/houtai/
│   ├── common/           # 公共类
│   ├── config/           # 配置类
│   ├── controller/       # 控制器层
│   ├── entity/          # 实体类
│   ├── mapper/          # MyBatis Mapper接口
│   ├── service/         # 服务层
│   └── util/           # 工具类
└── src/main/resources/
    ├── mapper/          # MyBatis XML映射文件
    └── application.properties
```

## 技术栈

- Spring Boot 4.0.1
- MyBatis 3.0.3
- MySQL 8.0+
- JWT (jjwt 0.12.3)
- Lombok

## 数据库配置

1. 创建数据库并执行初始化脚本：

```bash
mysql -u root -p < init.sql
```

2. 修改 `application.properties` 中的数据库连接信息：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/student_management_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=你的密码
```

## 运行项目

1. 确保MySQL服务已启动
2. 执行数据库初始化脚本
3. 运行Spring Boot应用：

```bash
./mvnw spring-boot:run
```

或者在IDE中运行 `SpringbootHoutaiApplication` 类

## API接口

服务运行在 `http://localhost:8080`

### 认证接口

- `POST /api/auth/login` - 用户登录
- `GET /api/auth/userinfo` - 获取用户信息
- `POST /api/auth/logout` - 用户退出

### 学生管理

- `GET /api/students` - 获取学生列表
- `GET /api/students/{id}` - 获取学生详情
- `POST /api/students` - 创建学生
- `PUT /api/students/{id}` - 更新学生
- `DELETE /api/students/{id}` - 删除学生

### 教师管理

- `GET /api/teachers` - 获取教师列表
- `POST /api/teachers` - 创建教师
- `PUT /api/teachers/{id}` - 更新教师
- `DELETE /api/teachers/{id}` - 删除教师

### 班级管理

- `GET /api/classes` - 获取班级列表
- `POST /api/classes` - 创建班级
- `PUT /api/classes/{id}` - 更新班级
- `DELETE /api/classes/{id}` - 删除班级

### 课程管理

- `GET /api/courses` - 获取课程列表
- `POST /api/courses` - 创建课程
- `PUT /api/courses/{id}` - 更新课程
- `DELETE /api/courses/{id}` - 删除课程

### 成绩管理

- `GET /api/grades` - 获取成绩列表
- `GET /api/grades/student/{studentId}` - 获取学生成绩
- `POST /api/grades` - 创建成绩
- `PUT /api/grades/{id}` - 更新成绩
- `DELETE /api/grades/{id}` - 删除成绩

### 选课管理

- `GET /api/course-selections` - 获取选课列表
- `GET /api/course-selections/student/{studentId}` - 获取学生选课
- `POST /api/course-selections` - 创建选课
- `DELETE /api/course-selections/{id}` - 删除选课（退选）

## 默认账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | 管理员 |
| teacher1 | admin123 | 教师 |
| student1 | admin123 | 学生 |

## 前后端对接

前端已配置为连接 `http://localhost:8080/api`，确保：
1. 后端服务运行在 8080 端口
2. MySQL数据库已启动并初始化
3. CORS配置允许跨域访问
