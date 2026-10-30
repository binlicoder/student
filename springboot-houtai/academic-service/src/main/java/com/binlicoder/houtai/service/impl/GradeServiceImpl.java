package com.binlicoder.houtai.service.impl;

import com.binlicoder.houtai.common.PageResult;
import com.binlicoder.houtai.entity.Grade;
import com.binlicoder.houtai.mapper.GradeMapper;
import com.binlicoder.houtai.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeMapper gradeMapper;

    @Override
    public PageResult<Grade> findAll(String studentNo, Integer courseId, String semester, Integer page, Integer pageSize) {
        page = page == null || page < 1 ? 1 : page;
        pageSize = pageSize == null || pageSize < 1 ? 10 : pageSize;
        Integer offset = (page - 1) * pageSize;
        return new PageResult<>(
            gradeMapper.findAll(studentNo, courseId, semester, offset, pageSize),
            gradeMapper.countAll(studentNo, courseId, semester),
            page,
            pageSize
        );
    }

    @Override
    public Grade findById(Integer id) {
        return gradeMapper.findById(id);
    }

    @Override
    public List<Grade> findByStudentId(Integer studentId) {
        return gradeMapper.findByStudentId(studentId);
    }

    @Override
    public Grade create(Grade grade) {
        gradeMapper.insert(grade);
        return grade;
    }

    @Override
    public Grade update(Grade grade) {
        gradeMapper.update(grade);
        return grade;
    }

    @Override
    public void delete(Integer id) {
        gradeMapper.delete(id);
    }
}
