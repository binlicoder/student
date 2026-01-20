package com.binlicoder.houtai.service.impl;

import com.binlicoder.houtai.common.PageResult;
import com.binlicoder.houtai.entity.CourseSelection;
import com.binlicoder.houtai.mapper.CourseSelectionMapper;
import com.binlicoder.houtai.service.CourseSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseSelectionServiceImpl implements CourseSelectionService {

    @Autowired
    private CourseSelectionMapper courseSelectionMapper;

    @Override
    public PageResult<CourseSelection> findAll(String studentNo, Integer courseId, String semester, Integer page, Integer pageSize) {
        page = page == null || page < 1 ? 1 : page;
        pageSize = pageSize == null || pageSize < 1 ? 10 : pageSize;
        Integer offset = (page - 1) * pageSize;
        return new PageResult<>(
            courseSelectionMapper.findAll(studentNo, courseId, semester, offset, pageSize),
            courseSelectionMapper.countAll(studentNo, courseId, semester),
            page,
            pageSize
        );
    }

    @Override
    public CourseSelection findById(Integer id) {
        return courseSelectionMapper.findById(id);
    }

    @Override
    public List<CourseSelection> findByStudentId(Integer studentId) {
        return courseSelectionMapper.findByStudentId(studentId);
    }

    @Override
    public CourseSelection create(CourseSelection courseSelection) {
        courseSelectionMapper.insert(courseSelection);
        return courseSelection;
    }

    @Override
    public void delete(Integer id) {
        courseSelectionMapper.delete(id);
    }
}
