# 学生信息管理系统 - API接口文档

## 基础信息

- **Base URL**: `http://localhost:3000/api`
- **认证方式**: Bearer Token (JWT)
- **Content-Type**: `application/json`

---

## 认证接口

### 1. 用户登录

**接口地址**: `POST /auth/login`

**请求参数**:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "username": "admin",
    "real_name": "系统管理员",
    "role": "admin"
  }
}
```

**默认账号**:
- 管理员: admin / admin123
- 教师: teacher1 / admin123
- 学生: student1 / admin123

---

### 2. 获取用户信息

**接口地址**: `GET /auth/userinfo`

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "real_name": "系统管理员",
    "role": "admin",
    "status": 1,
    "created_at": "2024-01-01T00:00:00.000Z"
  }
}
```

---

### 3. 用户退出

**接口地址**: `POST /auth/logout`

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "退出成功"
}
```

---

## 学生管理接口

### 1. 获取学生列表

**接口地址**: `GET /students`

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Number | 否 | 页码，默认1 |
| pageSize | Number | 否 | 每页数量，默认10 |
| studentNo | String | 否 | 学号（模糊查询） |
| name | String | 否 | 姓名（模糊查询） |
| classId | Number | 否 | 班级ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "student_no": "S2024001",
      "real_name": "张三",
      "username": "student1",
      "gender": "male",
      "class_id": 1,
      "class_name": "计算机科学与技术1班",
      "birthday": "2003-05-15",
      "phone": "13900139001",
      "email": "zhangsan@student.edu.cn",
      "address": "北京市朝阳区",
      "enrollment_date": "2024-09-01",
      "status": "studying"
    }
  ],
  "total": 2
}
```

---

### 2. 获取学生详情

**接口地址**: `GET /students/:id`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Number | 是 | 学生ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "student_no": "S2024001",
    "real_name": "张三",
    "username": "student1",
    "gender": "male",
    "class_id": 1,
    "class_name": "计算机科学与技术1班",
    "birthday": "2003-05-15",
    "phone": "13900139001",
    "email": "zhangsan@student.edu.cn",
    "address": "北京市朝阳区",
    "enrollment_date": "2024-09-01",
    "status": "studying"
  }
}
```

---

### 3. 创建学生

**接口地址**: `POST /students`

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
```json
{
  "student_no": "S2024003",
  "real_name": "王五",
  "username": "student3",
  "password": "123456",
  "gender": "male",
  "class_id": 1,
  "birthday": "2003-06-01",
  "phone": "13900139003",
  "email": "wangwu@student.edu.cn",
  "address": "北京市海淀区",
  "enrollment_date": "2024-09-01",
  "status": "studying"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 3
  }
}
```

---

### 4. 更新学生

**接口地址**: `PUT /students/:id`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Number | 是 | 学生ID |

**请求参数**:
```json
{
  "real_name": "张三丰",
  "gender": "male",
  "class_id": 1,
  "birthday": "2003-05-15",
  "phone": "13900139001",
  "email": "zhangsanfeng@student.edu.cn",
  "address": "北京市朝阳区",
  "enrollment_date": "2024-09-01",
  "status": "studying"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "更新成功"
}
```

---

### 5. 删除学生

**接口地址**: `DELETE /students/:id`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Number | 是 | 学生ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "删除成功"
}
```

---

## 教师管理接口

### 1. 获取教师列表

**接口地址**: `GET /teachers`

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Number | 否 | 页码，默认1 |
| pageSize | Number | 否 | 每页数量，默认10 |
| teacherNo | String | 否 | 工号（模糊查询） |
| name | String | 否 | 姓名（模糊查询） |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "teacher_no": "T2024001",
      "real_name": "张老师",
      "username": "teacher1",
      "gender": "male",
      "phone": "13800138001",
      "email": "zhang@school.edu.cn",
      "title": "副教授",
      "department": "计算机学院"
    }
  ],
  "total": 2
}
```

---

### 2. 创建教师

**接口地址**: `POST /teachers`

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
```json
{
  "teacher_no": "T2024003",
  "real_name": "王老师",
  "username": "teacher3",
  "password": "123456",
  "gender": "female",
  "phone": "13800138003",
  "email": "wang@school.edu.cn",
  "title": "讲师",
  "department": "软件学院"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 3
  }
}
```

---

### 3. 更新教师

**接口地址**: `PUT /teachers/:id`

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
```json
{
  "real_name": "张老师",
  "gender": "male",
  "phone": "13800138001",
  "email": "zhang@school.edu.cn",
  "title": "教授",
  "department": "计算机学院"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "更新成功"
}
```

---

### 4. 删除教师

**接口地址**: `DELETE /teachers/:id`

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "删除成功"
}
```

---

## 班级管理接口

### 1. 获取班级列表

**接口地址**: `GET /classes`

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Number | 否 | 页码，默认1 |
| pageSize | Number | 否 | 每页数量，默认10 |
| classCode | String | 否 | 班级编号（模糊查询） |
| className | String | 否 | 班级名称（模糊查询） |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "class_code": "CS2024001",
      "class_name": "计算机科学与技术1班",
      "grade": "2024级",
      "major": "计算机科学与技术",
      "class_size": 30,
      "head_teacher_id": 1,
      "head_teacher_name": "张老师"
    }
  ],
  "total": 3
}
```

---

### 2. 创建班级

**接口地址**: `POST /classes`

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
```json
{
  "class_code": "CS2024002",
  "class_name": "计算机科学与技术2班",
  "grade": "2024级",
  "major": "计算机科学与技术",
  "class_size": 30,
  "head_teacher_id": 1
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 4
  }
}
```

---

### 3. 更新班级

**接口地址**: `PUT /classes/:id`

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
```json
{
  "class_name": "计算机科学与技术1班",
  "grade": "2024级",
  "major": "计算机科学与技术",
  "class_size": 35,
  "head_teacher_id": 1
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "更新成功"
}
```

---

### 4. 删除班级

**接口地址**: `DELETE /classes/:id`

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "删除成功"
}
```

---

## 课程管理接口

### 1. 获取课程列表

**接口地址**: `GET /courses`

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Number | 否 | 页码，默认1 |
| pageSize | Number | 否 | 每页数量，默认10 |
| courseCode | String | 否 | 课程编号（模糊查询） |
| courseName | String | 否 | 课程名称（模糊查询） |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "course_code": "MATH1001",
      "course_name": "高等数学",
      "credit": 4.0,
      "hours": 64,
      "course_type": "required",
      "description": "高等数学基础课程"
    }
  ],
  "total": 8
}
```

---

### 2. 创建课程

**接口地址**: `POST /courses`

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
```json
{
  "course_code": "CS3002",
  "course_name": "移动应用开发",
  "credit": 3.0,
  "hours": 48,
  "course_type": "elective",
  "description": "移动应用开发技术"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 9
  }
}
```

---

### 3. 更新课程

**接口地址**: `PUT /courses/:id`

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
```json
{
  "course_name": "高等数学",
  "credit": 4.0,
  "hours": 64,
  "course_type": "required",
  "description": "高等数学基础课程"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "更新成功"
}
```

---

### 4. 删除课程

**接口地址**: `DELETE /courses/:id`

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "删除成功"
}
```

---

## 成绩管理接口

### 1. 获取成绩列表

**接口地址**: `GET /grades`

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Number | 否 | 页码，默认1 |
| pageSize | Number | 否 | 每页数量，默认10 |
| studentNo | String | 否 | 学号（模糊查询） |
| courseId | Number | 否 | 课程ID |
| semester | String | 否 | 学期 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "student_id": 1,
      "student_no": "S2024001",
      "student_name": "张三",
      "course_id": 1,
      "course_name": "高等数学",
      "teacher_id": 1,
      "teacher_name": "张老师",
      "semester": "2024-2025-1",
      "usual_score": 85.0,
      "exam_score": 88.0,
      "total_score": 86.5,
      "grade_level": "A",
      "remark": ""
    }
  ],
  "total": 6
}
```

---

### 2. 创建成绩

**接口地址**: `POST /grades`

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
```json
{
  "student_id": 1,
  "course_id": 2,
  "teacher_id": 1,
  "semester": "2024-2025-1",
  "usual_score": 90.0,
  "exam_score": 92.0,
  "total_score": 91.0,
  "grade_level": "A",
  "remark": ""
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 7
  }
}
```

---

### 3. 更新成绩

**接口地址**: `PUT /grades/:id`

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
```json
{
  "usual_score": 85.0,
  "exam_score": 88.0,
  "total_score": 86.5,
  "grade_level": "A",
  "remark": "优秀"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "更新成功"
}
```

---

### 4. 删除成绩

**接口地址**: `DELETE /grades/:id`

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "删除成功"
}
```

---

### 5. 获取学生成绩

**接口地址**: `GET /grades/student/:studentId`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| studentId | Number | 是 | 学生ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "student_id": 1,
      "course_id": 1,
      "course_name": "高等数学",
      "teacher_id": 1,
      "teacher_name": "张老师",
      "semester": "2024-2025-1",
      "usual_score": 85.0,
      "exam_score": 88.0,
      "total_score": 86.5,
      "grade_level": "A",
      "remark": ""
    }
  ]
}
```

---

## 选课管理接口

### 1. 获取选课列表

**接口地址**: `GET /course-selections`

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Number | 否 | 页码，默认1 |
| pageSize | Number | 否 | 每页数量，默认10 |
| studentNo | String | 否 | 学号（模糊查询） |
| courseId | Number | 否 | 课程ID |
| semester | String | 否 | 学期 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "student_id": 1,
      "student_no": "S2024001",
      "student_name": "张三",
      "course_id": 1,
      "course_name": "高等数学",
      "teacher_id": 1,
      "teacher_name": "张老师",
      "semester": "2024-2025-1",
      "status": "completed",
      "selection_time": "2024-09-01T00:00:00.000Z"
    }
  ],
  "total": 6
}
```

---

### 2. 创建选课

**接口地址**: `POST /course-selections`

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
```json
{
  "student_id": 1,
  "course_id": 5,
  "teacher_id": 1,
  "semester": "2024-2025-2"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "选课成功",
  "data": {
    "id": 7
  }
}
```

---

### 3. 删除选课（退选）

**接口地址**: `DELETE /course-selections/:id`

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "退选成功"
}
```

---

### 4. 获取学生选课

**接口地址**: `GET /course-selections/student/:studentId`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| studentId | Number | 是 | 学生ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "student_id": 1,
      "course_id": 1,
      "course_name": "高等数学",
      "teacher_id": 1,
      "teacher_name": "张老师",
      "semester": "2024-2025-1",
      "status": "completed",
      "selection_time": "2024-09-01T00:00:00.000Z"
    }
  ]
}
```

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 未授权，需要登录 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 数据字典

### 性别 (gender)
- `male`: 男
- `female`: 女
- `other`: 其他

### 用户角色 (role)
- `admin`: 管理员
- `teacher`: 教师
- `student`: 学生

### 学籍状态 (status)
- `studying`: 在读
- `graduated`: 毕业
- `suspended`: 休学
- `withdrawn`: 退学

### 课程类型 (course_type)
- `required`: 必修课
- `elective`: 选修课
- `practical`: 实践课

### 选课状态 (status)
- `selected`: 已选
- `dropped`: 已退
- `completed`: 已完成

### 成绩等级 (grade_level)
- `A`: 90-100分
- `B`: 80-89分
- `C`: 70-79分
- `D`: 60-69分
- `F`: 0-59分
