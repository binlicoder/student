# 学生信息管理系统

一个基于 Vue3 + Express + MySQL 的学生信息管理系统，实现了学生、教师、班级、课程、成绩和选课的完整管理功能。

## 技术栈

### 前端
- Vue 3
- Vue Router 4
- Element Plus
- Axios
- Vite

### 后端
- Node.js
- Express
- MySQL
- JWT (身份认证)
- bcryptjs (密码加密)

## 项目结构

```
vue-admin/
├── backend/                 # 后端项目
│   ├── config/             # 配置文件
│   │   └── db.js          # 数据库配置
│   ├── controllers/        # 控制器
│   │   ├── authController.js
│   │   ├── studentController.js
│   │   ├── teacherController.js
│   │   ├── classController.js
│   │   ├── courseController.js
│   │   ├── gradeController.js
│   │   └── selectionController.js
│   ├── middleware/         # 中间件
│   │   └── auth.js        # JWT认证中间件
│   ├── routes/             # 路由
│   │   ├── auth.js
│   │   ├── student.js
│   │   ├── teacher.js
│   │   ├── class.js
│   │   ├── course.js
│   │   ├── grade.js
│   │   └── selection.js
│   ├── .env               # 环境变量
│   ├── package.json
│   └── server.js          # 入口文件
├── src/                   # 前端源码
│   ├── api/               # API接口
│   │   ├── auth.js
│   │   ├── student.js
│   │   ├── teacher.js
│   │   ├── class.js
│   │   ├── course.js
│   │   ├── grade.js
│   │   └── selection.js
│   ├── router/            # 路由配置
│   │   └── index.js
│   ├── utils/             # 工具函数
│   │   └── request.js     # Axios封装
│   ├── views/             # 页面组件
│   │   ├── Login.vue
│   │   ├── Layout.vue
│   │   ├── Dashboard.vue
│   │   ├── Students.vue
│   │   ├── Teachers.vue
│   │   ├── Classes.vue
│   │   ├── Courses.vue
│   │   ├── Grades.vue
│   │   └── CourseSelection.vue
│   ├── App.vue
│   └── main.js
├── database.sql           # 数据库脚本
├── API文档.md            # API接口文档
└── package.json
```

## 快速开始

### 1. 数据库配置

首先需要安装 MySQL 数据库，然后执行以下步骤：

1. 创建数据库并导入数据：
```bash
mysql -u root -p < database.sql
```

2. 修改后端配置文件 `backend/.env`，配置数据库连接信息：
```env
DB_HOST=localhost
DB_USER=root
DB_PASSWORD=your_password
DB_NAME=student_management_system
DB_PORT=3306

JWT_SECRET=your-secret-key-change-in-production
JWT_EXPIRES_IN=7d

PORT=3000
```

### 2. 安装依赖

#### 前端依赖安装
```bash
npm install
```

#### 后端依赖安装
```bash
cd backend
npm install
```

### 3. 启动项目

#### 启动后端服务
```bash
cd backend
npm start
```

后端服务将运行在 `http://localhost:3000`

#### 启动前端服务
```bash
npm run dev
```

前端服务将运行在 `http://localhost:5173`（或其他Vite分配的端口）

### 4. 访问系统

在浏览器中打开 `http://localhost:5173`，使用以下账号登录：

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 教师 | teacher1 | admin123 |
| 学生 | student1 | admin123 |

## 功能模块

### 1. 用户认证
- 用户登录
- JWT Token认证
- 用户信息获取
- 用户退出

### 2. 学生管理
- 学生列表查询（支持分页、搜索）
- 添加学生
- 编辑学生信息
- 删除学生
- 学生详情查看

### 3. 教师管理
- 教师列表查询（支持分页、搜索）
- 添加教师
- 编辑教师信息
- 删除教师
- 教师详情查看

### 4. 班级管理
- 班级列表查询（支持分页、搜索）
- 添加班级
- 编辑班级信息
- 删除班级
- 班级详情查看

### 5. 课程管理
- 课程列表查询（支持分页、搜索）
- 添加课程
- 编辑课程信息
- 删除课程
- 课程详情查看

### 6. 成绩管理
- 成绩列表查询（支持分页、搜索）
- 录入成绩
- 编辑成绩
- 删除成绩
- 查看学生成绩

### 7. 选课管理
- 选课列表查询（支持分页、搜索）
- 学生选课
- 退选课程
- 查看学生选课

## 数据库设计

系统包含以下7张表：

1. **users** - 用户表：存储系统用户信息
2. **classes** - 班级表：存储班级信息
3. **teachers** - 教师表：存储教师详细信息
4. **students** - 学生表：存储学生详细信息
5. **courses** - 课程表：存储课程信息
6. **course_selections** - 选课表：存储学生选课信息
7. **grades** - 成绩表：存储学生成绩信息

详细的数据库设计请参考 [database.sql](./database.sql) 文件。

## API接口文档

完整的API接口文档请参考 [API文档.md](./API文档.md)。

## 开发说明

### 前端开发

前端使用 Vue 3 Composition API 开发，主要技术点：

- Vue Router 4 路由管理
- Element Plus UI组件库
- Axios HTTP请求封装
- 响应式数据管理

### 后端开发

后端使用 Express 框架，主要技术点：

- RESTful API设计
- JWT身份认证
- MySQL数据库操作
- 中间件机制
- 错误处理

## 注意事项

1. **密码安全**：生产环境请修改 `.env` 文件中的 `JWT_SECRET` 和数据库密码
2. **CORS配置**：后端已配置CORS，允许前端跨域访问
3. **数据库连接**：确保MySQL服务已启动，并且配置正确
4. **端口占用**：如果3000或5173端口被占用，请修改相应的配置

## 常见问题

### 1. 登录失败
- 检查数据库是否正确导入
- 检查 `.env` 文件中的数据库配置是否正确
- 确认用户名和密码是否正确

### 2. API请求失败
- 检查后端服务是否正常启动
- 检查 `src/utils/request.js` 中的 `baseURL` 配置
- 查看浏览器控制台和后端日志

### 3. 数据库连接失败
- 确认MySQL服务是否启动
- 检查数据库用户名和密码是否正确
- 确认数据库名称是否正确

## 许可证

MIT License
