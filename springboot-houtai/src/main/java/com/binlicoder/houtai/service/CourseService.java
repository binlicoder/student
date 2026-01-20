package com.binlicoder.houtai.service;

import com.binlicoder.houtai.common.PageResult;
import com.binlicoder.houtai.entity.Course;

public interface CourseService {
    PageResult<Course> findAll(String courseCode, String courseName, Integer page, Integer pageSize);
    Course findById(Integer id);
    Course create(Course course);
    Course update(Course course);
    void delete(Integer id);
}
