package com.binlicoder.houtai.controller;

import com.binlicoder.houtai.common.PageResult;
import com.binlicoder.houtai.common.Result;
import com.binlicoder.houtai.entity.Course;
import com.binlicoder.houtai.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public Result<Map<String, Object>> getCourses(
            @RequestParam(required = false) String courseCode,
            @RequestParam(required = false) String courseName,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<Course> result = courseService.findAll(courseCode, courseName, page, pageSize);
        Map<String, Object> data = new HashMap<>();
        data.put("data", result.getData());
        data.put("total", result.getTotal());
        data.put("page", result.getPage());
        data.put("pageSize", result.getPageSize());
        return Result.success(data);
    }

    @PostMapping
    public Result<Map<String, Integer>> createCourse(@RequestBody Map<String, Object> request) {
        Course course = new Course();
        course.setCourseName((String) request.get("course_name"));
        course.setCourseCode((String) request.get("course_code"));
        course.setCredit(request.get("credit") != null ? 
            Double.parseDouble(request.get("credit").toString()) : null);
        course.setHours(request.get("hours") != null ? 
            Integer.parseInt(request.get("hours").toString()) : null);
        course.setCourseType((String) request.get("course_type"));
        course.setDescription((String) request.get("description"));
        courseService.create(course);

        Map<String, Integer> data = new HashMap<>();
        data.put("id", course.getId());
        return Result.success("创建成功", data);
    }

    @PutMapping("/{id}")
    public Result updateCourse(@PathVariable Integer id, @RequestBody Map<String, Object> request) {
        Course course = courseService.findById(id);
        if (course == null) {
            return Result.error(404, "课程不存在");
        }

        course.setCourseName((String) request.get("course_name"));
        course.setCredit(request.get("credit") != null ? 
            Double.parseDouble(request.get("credit").toString()) : null);
        course.setHours(request.get("hours") != null ? 
            Integer.parseInt(request.get("hours").toString()) : null);
        course.setCourseType((String) request.get("course_type"));
        course.setDescription((String) request.get("description"));
        courseService.update(course);

        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteCourse(@PathVariable Integer id) {
        Course course = courseService.findById(id);
        if (course == null) {
            return Result.error(404, "课程不存在");
        }
        courseService.delete(id);
        return Result.success("删除成功");
    }
}
