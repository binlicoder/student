package com.binlicoder.houtai.service.impl;

import com.binlicoder.houtai.common.PageResult;
import com.binlicoder.houtai.entity.Course;
import com.binlicoder.houtai.mapper.CourseMapper;
import com.binlicoder.houtai.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public PageResult<Course> findAll(String courseCode, String courseName, Integer page, Integer pageSize) {
        page = page == null || page < 1 ? 1 : page;
        pageSize = pageSize == null || pageSize < 1 ? 10 : pageSize;
        Integer offset = (page - 1) * pageSize;
        return new PageResult<>(
            courseMapper.findAll(courseCode, courseName, offset, pageSize),
            courseMapper.countAll(courseCode, courseName),
            page,
            pageSize
        );
    }

    @Override
    public Course findById(Integer id) {
        return courseMapper.findById(id);
    }

    @Override
    public Course create(Course course) {
        courseMapper.insert(course);
        return course;
    }

    @Override
    public Course update(Course course) {
        courseMapper.update(course);
        return course;
    }

    @Override
    public void delete(Integer id) {
        courseMapper.delete(id);
    }
}
