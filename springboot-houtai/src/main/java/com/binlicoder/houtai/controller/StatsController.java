package com.binlicoder.houtai.controller;

import com.binlicoder.houtai.common.Result;
import com.binlicoder.houtai.mapper.StudentMapper;
import com.binlicoder.houtai.mapper.TeacherMapper;
import com.binlicoder.houtai.mapper.CourseMapper;
import com.binlicoder.houtai.mapper.ClassMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ClassMapper classMapper;

    @GetMapping
    public Result<Map<String, Integer>> getStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("studentCount", studentMapper.countAll(null, null, null));
        stats.put("teacherCount", teacherMapper.countAll(null, null));
        stats.put("courseCount", courseMapper.countAll(null, null));
        stats.put("classCount", classMapper.countAll(null, null));
        return Result.success(stats);
    }
}
