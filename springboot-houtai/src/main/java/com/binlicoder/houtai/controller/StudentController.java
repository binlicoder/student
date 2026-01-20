package com.binlicoder.houtai.controller;

import com.binlicoder.houtai.common.PageResult;
import com.binlicoder.houtai.common.Result;
import com.binlicoder.houtai.entity.Student;
import com.binlicoder.houtai.entity.User;
import com.binlicoder.houtai.service.StudentService;
import com.binlicoder.houtai.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @GetMapping
    public Result<Map<String, Object>> getStudents(
            @RequestParam(required = false) String studentNo,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer classId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<Student> result = studentService.findAll(studentNo, name, classId, page, pageSize);
        Map<String, Object> data = new HashMap<>();
        data.put("data", result.getData());
        data.put("total", result.getTotal());
        data.put("page", result.getPage());
        data.put("pageSize", result.getPageSize());
        return Result.success(data);
    }

    @GetMapping("/{id}")
    public Result<Student> getStudentById(@PathVariable Integer id) {
        Student student = studentService.findById(id);
        if (student == null) {
            return Result.error(404, "学生不存在");
        }
        return Result.success(student);
    }

    @PostMapping
    public Result<Map<String, Integer>> createStudent(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        String password = (String) request.get("password");
        String realName = (String) request.get("real_name");

        if (userService.findByUsername(username) != null) {
            return Result.error(400, "用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRealName(realName);
        user.setRole("student");
        user.setStatus(1);
        userService.create(user);

        Student student = new Student();
        student.setUserId(user.getId());
        student.setStudentNo((String) request.get("student_no"));
        student.setClassId((Integer) request.get("class_id"));
        student.setGender((String) request.get("gender"));
        student.setBirthday(request.get("birthday") != null ? 
            java.time.LocalDate.parse(request.get("birthday").toString()) : null);
        student.setPhone((String) request.get("phone"));
        student.setEmail((String) request.get("email"));
        student.setAddress((String) request.get("address"));
        student.setEnrollmentDate(request.get("enrollment_date") != null ? 
            java.time.LocalDate.parse(request.get("enrollment_date").toString()) : null);
        student.setStatus((String) request.get("status"));
        studentService.create(student);

        Map<String, Integer> data = new HashMap<>();
        data.put("id", student.getId());
        return Result.success("创建成功", data);
    }

    @PutMapping("/{id}")
    public Result<String> updateStudent(@PathVariable Integer id, @RequestBody Map<String, Object> request) {
        Student student = studentService.findById(id);
        if (student == null) {
            return Result.error(404, "学生不存在");
        }

        student.setClassId((Integer) request.get("class_id"));
        student.setGender((String) request.get("gender"));
        if (request.get("birthday") != null) {
            student.setBirthday(java.time.LocalDate.parse(request.get("birthday").toString()));
        }
        student.setPhone((String) request.get("phone"));
        student.setEmail((String) request.get("email"));
        student.setAddress((String) request.get("address"));
        if (request.get("enrollment_date") != null) {
            student.setEnrollmentDate(java.time.LocalDate.parse(request.get("enrollment_date").toString()));
        }
        student.setStatus((String) request.get("status"));
        studentService.update(student);

        User user = userService.findById(student.getUserId());
        if (user != null && request.get("real_name") != null) {
            user.setRealName((String) request.get("real_name"));
            userService.update(user);
        }

        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result deleteStudent(@PathVariable Integer id) {
        Student student = studentService.findById(id);
        if (student == null) {
            return Result.error(404, "学生不存在");
        }
        studentService.delete(id);
        userService.delete(student.getUserId());
        return Result.success("删除成功");
    }
}
