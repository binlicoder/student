package com.binlicoder.houtai.controller;

import com.binlicoder.houtai.common.PageResult;
import com.binlicoder.houtai.common.Result;
import com.binlicoder.houtai.entity.Grade;
import com.binlicoder.houtai.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @GetMapping
    public Result<Map<String, Object>> getGrades(
            @RequestParam(required = false) String studentNo,
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) String semester,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<Grade> result = gradeService.findAll(studentNo, courseId, semester, page, pageSize);
        Map<String, Object> data = new HashMap<>();
        data.put("data", result.getData());
        data.put("total", result.getTotal());
        data.put("page", result.getPage());
        data.put("pageSize", result.getPageSize());
        return Result.success(data);
    }

    @GetMapping("/student/{studentId}")
    public Result<List<Grade>> getGradesByStudentId(@PathVariable Integer studentId) {
        List<Grade> grades = gradeService.findByStudentId(studentId);
        return Result.success(grades);
    }

    @PostMapping
    public Result<Map<String, Integer>> createGrade(@RequestBody Map<String, Object> request) {
        Grade grade = new Grade();
        grade.setStudentId((Integer) request.get("student_id"));
        grade.setCourseId((Integer) request.get("course_id"));
        grade.setTeacherId((Integer) request.get("teacher_id"));
        grade.setSemester((String) request.get("semester"));
        grade.setUsualScore(request.get("usual_score") != null ? 
            java.math.BigDecimal.valueOf(Double.parseDouble(request.get("usual_score").toString())) : null);
        grade.setExamScore(request.get("exam_score") != null ? 
            java.math.BigDecimal.valueOf(Double.parseDouble(request.get("exam_score").toString())) : null);
        grade.setTotalScore(request.get("total_score") != null ? 
            java.math.BigDecimal.valueOf(Double.parseDouble(request.get("total_score").toString())) : null);
        grade.setGradeLevel((String) request.get("grade_level"));
        grade.setRemark((String) request.get("remark"));
        gradeService.create(grade);

        Map<String, Integer> data = new HashMap<>();
        data.put("id", grade.getId());
        return Result.success("创建成功", data);
    }

    @PutMapping("/{id}")
    public Result updateGrade(@PathVariable Integer id, @RequestBody Map<String, Object> request) {
        Grade grade = gradeService.findById(id);
        if (grade == null) {
            return Result.error(404, "成绩不存在");
        }

        grade.setUsualScore(request.get("usual_score") != null ? 
            java.math.BigDecimal.valueOf(Double.parseDouble(request.get("usual_score").toString())) : null);
        grade.setExamScore(request.get("exam_score") != null ? 
            java.math.BigDecimal.valueOf(Double.parseDouble(request.get("exam_score").toString())) : null);
        grade.setTotalScore(request.get("total_score") != null ? 
            java.math.BigDecimal.valueOf(Double.parseDouble(request.get("total_score").toString())) : null);
        grade.setGradeLevel((String) request.get("grade_level"));
        grade.setRemark((String) request.get("remark"));
        gradeService.update(grade);

        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteGrade(@PathVariable Integer id) {
        Grade grade = gradeService.findById(id);
        if (grade == null) {
            return Result.error(404, "成绩不存在");
        }
        gradeService.delete(id);
        return Result.success("删除成功");
    }
}
