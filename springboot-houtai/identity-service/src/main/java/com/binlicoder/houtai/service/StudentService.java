package com.binlicoder.houtai.service;

import com.binlicoder.houtai.common.PageResult;
import com.binlicoder.houtai.entity.Student;

public interface StudentService {
    PageResult<Student> findAll(String studentNo, String name, Integer classId, Integer page, Integer pageSize);
    Student findById(Integer id);
    Student findByUserId(Integer userId);
    Student create(Student student);
    Student update(Student student);
    void delete(Integer id);
}
