package com.binlicoder.houtai.service;

import com.binlicoder.houtai.common.PageResult;
import com.binlicoder.houtai.entity.Teacher;

public interface TeacherService {
    PageResult<Teacher> findAll(String teacherNo, String name, Integer page, Integer pageSize);
    Teacher findById(Integer id);
    Teacher findByUserId(Integer userId);
    Teacher create(Teacher teacher);
    Teacher update(Teacher teacher);
    void delete(Integer id);
}
