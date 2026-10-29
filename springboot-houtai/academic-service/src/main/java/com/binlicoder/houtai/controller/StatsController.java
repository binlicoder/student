package com.binlicoder.houtai.controller;

import com.binlicoder.houtai.common.Result;
import com.binlicoder.houtai.mapper.ClassMapper;
import com.binlicoder.houtai.mapper.CourseMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class StatsController {
    private final ClassMapper classes;
    private final CourseMapper courses;
    public StatsController(ClassMapper classes, CourseMapper courses) {
        this.classes = classes;
        this.courses = courses;
    }
    @GetMapping("/api/stats")
    public Result<Map<String, Integer>> counts() {
        return Result.success(Map.of("classCount", classes.countAll(null, null),
                "courseCount", courses.countAll(null, null)));
    }
}
