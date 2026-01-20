package com.binlicoder.houtai.controller;

import com.binlicoder.houtai.common.PageResult;
import com.binlicoder.houtai.common.Result;
import com.binlicoder.houtai.entity.Teacher;
import com.binlicoder.houtai.entity.User;
import com.binlicoder.houtai.service.TeacherService;
import com.binlicoder.houtai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private UserService userService;

    @GetMapping
    public Result<Map<String, Object>> getTeachers(
            @RequestParam(required = false) String teacherNo,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<Teacher> result = teacherService.findAll(teacherNo, name, page, pageSize);
        Map<String, Object> data = new HashMap<>();
        data.put("data", result.getData());
        data.put("total", result.getTotal());
        data.put("page", result.getPage());
        data.put("pageSize", result.getPageSize());
        return Result.success(data);
    }

    @PostMapping
    public Result<Map<String, Integer>> createTeacher(@RequestBody Map<String, Object> request) {
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
        user.setRole("teacher");
        user.setStatus(1);
        userService.create(user);

        Teacher teacher = new Teacher();
        teacher.setUserId(user.getId());
        teacher.setTeacherNo((String) request.get("teacher_no"));
        teacher.setGender((String) request.get("gender"));
        teacher.setPhone((String) request.get("phone"));
        teacher.setEmail((String) request.get("email"));
        teacher.setTitle((String) request.get("title"));
        teacher.setDepartment((String) request.get("department"));
        teacherService.create(teacher);

        Map<String, Integer> data = new HashMap<>();
        data.put("id", teacher.getId());
        return Result.success("创建成功", data);
    }

    @PutMapping("/{id}")
    public Result<String> updateTeacher(@PathVariable Integer id, @RequestBody Map<String, Object> request) {
        Teacher teacher = teacherService.findById(id);
        if (teacher == null) {
            return Result.error(404, "教师不存在");
        }

        teacher.setGender((String) request.get("gender"));
        teacher.setPhone((String) request.get("phone"));
        teacher.setEmail((String) request.get("email"));
        teacher.setTitle((String) request.get("title"));
        teacher.setDepartment((String) request.get("department"));
        teacherService.update(teacher);

        User user = userService.findById(teacher.getUserId());
        if (user != null && request.get("real_name") != null) {
            user.setRealName((String) request.get("real_name"));
            userService.update(user);
        }

        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result deleteTeacher(@PathVariable Integer id) {
        Teacher teacher = teacherService.findById(id);
        if (teacher == null) {
            return Result.error(404, "教师不存在");
        }
        teacherService.delete(id);
        userService.delete(teacher.getUserId());
        return Result.success("删除成功");
    }
}
