package com.binlicoder.houtai.controller;

import com.binlicoder.houtai.common.PageResult;
import com.binlicoder.houtai.common.Result;
import com.binlicoder.houtai.entity.Class;
import com.binlicoder.houtai.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    @Autowired
    private ClassService classService;

    @GetMapping
    public Result<Map<String, Object>> getClasses(
            @RequestParam(required = false) String classCode,
            @RequestParam(required = false) String className,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<Class> result = classService.findAll(classCode, className, page, pageSize);
        Map<String, Object> data = new HashMap<>();
        data.put("data", result.getData());
        data.put("total", result.getTotal());
        data.put("page", result.getPage());
        data.put("pageSize", result.getPageSize());
        return Result.success(data);
    }

    @PostMapping
    public Result<Map<String, Integer>> createClass(@RequestBody Map<String, Object> request) {
        Class classEntity = new Class();
        classEntity.setClassName((String) request.get("class_name"));
        classEntity.setClassCode((String) request.get("class_code"));
        classEntity.setGrade((String) request.get("grade"));
        classEntity.setMajor((String) request.get("major"));
        classEntity.setHeadTeacherId((Integer) request.get("head_teacher_id"));
        classEntity.setClassSize((Integer) request.get("class_size"));
        classService.create(classEntity);

        Map<String, Integer> data = new HashMap<>();
        data.put("id", classEntity.getId());
        return Result.success("创建成功", data);
    }

    @PutMapping("/{id}")
    public Result updateClass(@PathVariable Integer id, @RequestBody Map<String, Object> request) {
        Class classEntity = classService.findById(id);
        if (classEntity == null) {
            return Result.error(404, "班级不存在");
        }

        classEntity.setClassName((String) request.get("class_name"));
        classEntity.setGrade((String) request.get("grade"));
        classEntity.setMajor((String) request.get("major"));
        classEntity.setHeadTeacherId((Integer) request.get("head_teacher_id"));
        classEntity.setClassSize((Integer) request.get("class_size"));
        classService.update(classEntity);

        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteClass(@PathVariable Integer id) {
        Class classEntity = classService.findById(id);
        if (classEntity == null) {
            return Result.error(404, "班级不存在");
        }
        classService.delete(id);
        return Result.success("删除成功");
    }
}
