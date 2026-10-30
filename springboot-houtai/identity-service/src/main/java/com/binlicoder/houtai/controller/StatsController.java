package com.binlicoder.houtai.controller;

import com.binlicoder.houtai.common.Result;
import com.binlicoder.houtai.mapper.StudentMapper;
import com.binlicoder.houtai.mapper.TeacherMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class StatsController {
    private final StudentMapper students;
    private final TeacherMapper teachers;
    public StatsController(StudentMapper students, TeacherMapper teachers) {
        this.students = students;
        this.teachers = teachers;
    }
    @GetMapping("/api/stats")
    public Result<Map<String, Integer>> counts() {
        return Result.success(Map.of("studentCount", students.countAll(null, null, null),
                "teacherCount", teachers.countAll(null, null)));
    }
}
