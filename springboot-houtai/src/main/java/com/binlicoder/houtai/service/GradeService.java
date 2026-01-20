package com.binlicoder.houtai.service;

import com.binlicoder.houtai.common.PageResult;
import com.binlicoder.houtai.entity.Grade;

import java.util.List;

public interface GradeService {
    PageResult<Grade> findAll(String studentNo, Integer courseId, String semester, Integer page, Integer pageSize);
    Grade findById(Integer id);
    List<Grade> findByStudentId(Integer studentId);
    Grade create(Grade grade);
    Grade update(Grade grade);
    void delete(Integer id);
}
