# 学生信息管理系统 (Student Information Management System)

这是一个现代化的学生信息管理系统，包含完整的前端和后端实现。本项目提供了一个基于 **Vue 3** 的前端界面，并提供了两种后端实现供选择：**Spring Boot** (Java) 或 **Node.js** (Express)。

## ✨ 主要功能

系统包含以下核心模块：
- **用户认证**：登录、注销、JWT Token 认证。
- **学生管理**：学生信息的增删改查。
- **教师管理**：教师信息的增删改查。
- **班级管理**：班级信息的管理。
- **课程管理**：课程信息的维护。
- **成绩管理**：学生成绩录入与查询。
- **选课管理**：学生选课及退课功能。

## 🛠 技术栈

### 前端 (`vue-admin`)
- **框架**: Vue 3
- **构建工具**: Vite
- **UI 组件库**: Element Plus
- **路由**: Vue Router 4
- **HTTP 客户端**: Axios

### 后端选项 1 (`springboot-houtai`)
- **框架**: Spring Boot
- **ORM**: MyBatis
- **数据库**: MySQL
- **工具**: Lombok, Maven

### 后端选项 2 (`vue-admin/backend`)
- **框架**: Express.js (Node.js)
- **数据库**: MySQL
- **认证**: JWT

---

## 🚀 快速开始

### 1. 环境准备
- Node.js (v16+)
- JDK 17+ (如果使用 Java 后端)
- MySQL 8.0+

### 2. 数据库配置
请根据你选择的后端，执行相应的 SQL 初始化脚本。

- **如果你选择 Spring Boot 后端**:
  使用 MySQL 客户端执行 `springboot-houtai/init.sql` 脚本。

- **如果你选择 Node.js 后端**:
  使用 MySQL 客户端执行 `vue-admin/database.sql` 脚本。

### 3. 后端启动 (二选一)

#### 选项 A: 启动 Spring Boot 后端 (推荐)
1. 进入目录：`cd springboot-houtai`
2. 修改配置：在 `src/main/resources/application.properties` 中配置你的数据库用户名和密码。
3. 运行项目：
   ```bash
   ./mvnw spring-boot:run
   ```
   后端将在 `http://localhost:8080` 启动。

#### 选项 B: 启动 Node.js 后端
1. 进入目录：`cd vue-admin/backend`
2. 安装依赖：`npm install`
3. 修改配置：在 `.env` 文件中配置你的数据库连接信息。
4. 运行项目：
   ```bash
   npm start
   ```
   后端将在 `http://localhost:3000` 启动。

   **注意**：如果你使用 Node.js 后端，需要修改前端配置。打开 `vue-admin/src/utils/request.js`，将 `baseURL` 修改为：
   ```javascript
   baseURL: 'http://localhost:3000/api',
   ```

### 4. 前端启动
1. 进入前端目录：`cd vue-admin`
2. 安装依赖：
   ```bash
   npm install
   ```
3. 启动开发服务器：
   ```bash
   npm run dev
   ```
4. 访问地址：`http://localhost:5173`

---

## 👤 默认账号

系统预置了以下测试账号：

| 角色 | 用户名 | 密码 | 权限说明 |
|------|--------|------|----------|
| 管理员 | admin | admin123 | 拥有所有权限 |
| 教师 | teacher1 | admin123 | 仅限教师相关操作 |
| 学生 | student1 | admin123 | 仅限学生相关操作 |

## 📂 目录结构说明

```
.
├── springboot-houtai/     # Java Spring Boot 后端源码
│   ├── src/
│   ├── init.sql           # Java版数据库脚本
│   └── pom.xml
├── vue-admin/             # Vue3 前端源码
│   ├── backend/           # Node.js Express 后端源码
│   ├── src/
│   ├── database.sql       # Node版数据库脚本
│   └── package.json
└── README.md              # 项目说明文档
```

## 📄 许可证
MIT License
