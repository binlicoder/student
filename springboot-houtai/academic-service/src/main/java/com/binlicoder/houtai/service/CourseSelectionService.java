package com.binlicoder.houtai.service;

import com.binlicoder.houtai.common.PageResult;
import com.binlicoder.houtai.entity.CourseSelection;

import java.util.List;

public interface CourseSelectionService {
    PageResult<CourseSelection> findAll(String studentNo, Integer courseId, String semester, Integer page, Integer pageSize);
    CourseSelection findById(Integer id);
    List<CourseSelection> findByStudentId(Integer studentId);
    CourseSelection create(CourseSelection courseSelection);
    void delete(Integer id);
}
