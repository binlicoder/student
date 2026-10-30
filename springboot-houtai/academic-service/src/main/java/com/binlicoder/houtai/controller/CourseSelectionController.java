package com.binlicoder.houtai.controller;

import com.binlicoder.houtai.common.PageResult;
import com.binlicoder.houtai.common.Result;
import com.binlicoder.houtai.entity.CourseSelection;
import com.binlicoder.houtai.service.CourseSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/course-selections")
public class CourseSelectionController {

    @Autowired
    private CourseSelectionService courseSelectionService;

    @GetMapping
    public Result<Map<String, Object>> getCourseSelections(
            @RequestParam(required = false) String studentNo,
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) String semester,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<CourseSelection> result = courseSelectionService.findAll(studentNo, courseId, semester, page, pageSize);
        Map<String, Object> data = new HashMap<>();
        data.put("data", result.getData());
        data.put("total", result.getTotal());
        data.put("page", result.getPage());
        data.put("pageSize", result.getPageSize());
        return Result.success(data);
    }

    @GetMapping("/student/{studentId}")
    public Result<List<CourseSelection>> getCourseSelectionsByStudentId(@PathVariable Integer studentId) {
        List<CourseSelection> selections = courseSelectionService.findByStudentId(studentId);
        return Result.success(selections);
    }

    @PostMapping
    public Result<Map<String, Integer>> createCourseSelection(@RequestBody Map<String, Object> request) {
        CourseSelection selection = new CourseSelection();
        selection.setStudentId((Integer) request.get("student_id"));
        selection.setCourseId((Integer) request.get("course_id"));
        selection.setTeacherId((Integer) request.get("teacher_id"));
        selection.setSemester((String) request.get("semester"));
        selection.setStatus("selected");
        courseSelectionService.create(selection);

        Map<String, Integer> data = new HashMap<>();
        data.put("id", selection.getId());
        return Result.success("选课成功", data);
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteCourseSelection(@PathVariable Integer id) {
        CourseSelection selection = courseSelectionService.findById(id);
        if (selection == null) {
            return Result.error(404, "选课记录不存在");
        }
        courseSelectionService.delete(id);
        return Result.success("退选成功");
    }
}
