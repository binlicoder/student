-- 学生信息管理系统数据库初始化脚本
-- 数据库: student_management_system

-- 创建数据库
CREATE DATABASE IF NOT EXISTS student_management_system DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE student_management_system;

-- 1. 用户表 (users)
-- 存储系统用户信息（管理员、教师、学生）
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    role ENUM('admin', 'teacher', 'student') NOT NULL DEFAULT 'student' COMMENT '角色',
    status TINYINT DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 班级表 (classes)
-- 存储班级信息
CREATE TABLE IF NOT EXISTS classes (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '班级ID',
    class_name VARCHAR(50) NOT NULL COMMENT '班级名称',
    class_code VARCHAR(20) UNIQUE NOT NULL COMMENT '班级编号',
    grade VARCHAR(20) NOT NULL COMMENT '年级',
    major VARCHAR(50) NOT NULL COMMENT '专业',
    head_teacher_id INT COMMENT '班主任ID',
    class_size INT DEFAULT 0 COMMENT '班级人数',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (head_teacher_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- 3. 教师表 (teachers)
-- 存储教师详细信息
CREATE TABLE IF NOT EXISTS teachers (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '教师ID',
    user_id INT UNIQUE NOT NULL COMMENT '用户ID',
    teacher_no VARCHAR(20) UNIQUE NOT NULL COMMENT '教师工号',
    gender ENUM('male', 'female', 'other') DEFAULT 'other' COMMENT '性别',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    title VARCHAR(50) COMMENT '职称',
    department VARCHAR(100) COMMENT '所属院系',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师表';

-- 4. 学生表 (students)
-- 存储学生详细信息
CREATE TABLE IF NOT EXISTS students (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '学生ID',
    user_id INT UNIQUE NOT NULL COMMENT '用户ID',
    student_no VARCHAR(20) UNIQUE NOT NULL COMMENT '学号',
    class_id INT NOT NULL COMMENT '班级ID',
    gender ENUM('male', 'female', 'other') DEFAULT 'other' COMMENT '性别',
    birthday DATE COMMENT '出生日期',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    address VARCHAR(200) COMMENT '家庭住址',
    enrollment_date DATE COMMENT '入学日期',
    status ENUM('studying', 'graduated', 'suspended', 'withdrawn') DEFAULT 'studying' COMMENT '学籍状态',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表';

-- 5. 课程表 (courses)
-- 存储课程信息
CREATE TABLE IF NOT EXISTS courses (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '课程ID',
    course_name VARCHAR(100) NOT NULL COMMENT '课程名称',
    course_code VARCHAR(20) UNIQUE NOT NULL COMMENT '课程编号',
    credit DECIMAL(3,1) NOT NULL COMMENT '学分',
    hours INT NOT NULL COMMENT '学时',
    course_type ENUM('required', 'elective', 'practical') DEFAULT 'required' COMMENT '课程类型',
    description TEXT COMMENT '课程描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 6. 选课表 (course_selections)
-- 存储学生选课信息
CREATE TABLE IF NOT EXISTS course_selections (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '选课ID',
    student_id INT NOT NULL COMMENT '学生ID',
    course_id INT NOT NULL COMMENT '课程ID',
    teacher_id INT NOT NULL COMMENT '授课教师ID',
    semester VARCHAR(20) NOT NULL COMMENT '学期(如:2024-2025-1)',
    selection_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
    status ENUM('selected', 'dropped', 'completed') DEFAULT 'selected' COMMENT '选课状态',
    UNIQUE KEY uk_student_course_semester (student_id, course_id, semester),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='选课表';

-- 7. 成绩表 (grades)
-- 存储学生成绩信息
CREATE TABLE IF NOT EXISTS grades (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '成绩ID',
    student_id INT NOT NULL COMMENT '学生ID',
    course_id INT NOT NULL COMMENT '课程ID',
    teacher_id INT NOT NULL COMMENT '授课教师ID',
    semester VARCHAR(20) NOT NULL COMMENT '学期',
    usual_score DECIMAL(5,2) COMMENT '平时成绩',
    exam_score DECIMAL(5,2) COMMENT '考试成绩',
    total_score DECIMAL(5,2) COMMENT '总评成绩',
    grade_level VARCHAR(10) COMMENT '等级(A,B,C,D,F)',
    remark TEXT COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_student_course_semester (student_id, course_id, semester),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成绩表';

-- 插入初始数据

-- 插入管理员用户 (密码: admin123)
INSERT INTO users (username, password, real_name, role, status) VALUES
('admin', 'admin123', '系统管理员', 'admin', 1),
('teacher1', 'admin123', '张老师', 'teacher', 1),
('teacher2', 'admin123', '李老师', 'teacher', 1),
('student1', 'admin123', '张三', 'student', 1),
('student2', 'admin123', '李四', 'student', 1);

-- 插入班级信息
INSERT INTO classes (class_name, class_code, grade, major, class_size) VALUES
('计算机科学与技术1班', 'CS2024001', '2024级', '计算机科学与技术', 30),
('软件工程1班', 'SE2024001', '2024级', '软件工程', 28),
('人工智能1班', 'AI2024001', '2024级', '人工智能', 25);

-- 插入教师信息
INSERT INTO teachers (user_id, teacher_no, gender, phone, email, title, department) VALUES
(2, 'T2024001', 'male', '13800138001', 'zhang@school.edu.cn', '副教授', '计算机学院'),
(3, 'T2024002', 'female', '13800138002', 'li@school.edu.cn', '讲师', '软件学院');

-- 插入学生信息
INSERT INTO students (user_id, student_no, class_id, gender, birthday, phone, email, address, enrollment_date, status) VALUES
(4, 'S2024001', 1, 'male', '2003-05-15', '13900139001', 'zhangsan@student.edu.cn', '北京市朝阳区', '2024-09-01', 'studying'),
(5, 'S2024002', 1, 'female', '2003-08-20', '13900139002', 'lisi@student.edu.cn', '上海市浦东新区', '2024-09-01', 'studying');

-- 插入课程信息
INSERT INTO courses (course_name, course_code, credit, hours, course_type, description) VALUES
('高等数学', 'MATH1001', 4.0, 64, 'required', '高等数学基础课程'),
('程序设计基础', 'CS1001', 3.5, 56, 'required', 'C语言程序设计基础'),
('数据结构', 'CS2001', 4.0, 64, 'required', '数据结构与算法'),
('数据库原理', 'CS2002', 3.5, 56, 'required', '数据库系统原理'),
('计算机网络', 'CS2003', 3.0, 48, 'required', '计算机网络基础'),
('操作系统', 'CS2004', 4.0, 64, 'required', '操作系统原理'),
('Web开发技术', 'CS3001', 3.0, 48, 'elective', 'Web前端与后端开发'),
('人工智能导论', 'AI1001', 2.5, 40, 'elective', '人工智能基础概论');

-- 插入选课信息
INSERT INTO course_selections (student_id, course_id, teacher_id, semester, status) VALUES
(1, 1, 1, '2024-2025-1', 'completed'),
(1, 2, 1, '2024-2025-1', 'completed'),
(1, 3, 2, '2024-2025-1', 'completed'),
(2, 1, 1, '2024-2025-1', 'completed'),
(2, 2, 1, '2024-2025-1', 'completed'),
(2, 4, 2, '2024-2025-1', 'completed');

-- 插入成绩信息
INSERT INTO grades (student_id, course_id, teacher_id, semester, usual_score, exam_score, total_score, grade_level) VALUES
(1, 1, 1, '2024-2025-1', 85.0, 88.0, 86.5, 'A'),
(1, 2, 1, '2024-2025-1', 90.0, 92.0, 91.0, 'A'),
(1, 3, 2, '2024-2025-1', 78.0, 82.0, 80.0, 'B'),
(2, 1, 1, '2024-2025-1', 75.0, 80.0, 77.5, 'B'),
(2, 2, 1, '2024-2025-1', 82.0, 85.0, 83.5, 'B'),
(2, 4, 2, '2024-2025-1', 88.0, 90.0, 89.0, 'A');
