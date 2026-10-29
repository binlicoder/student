package com.binlicoder.houtai.service.impl;

import com.binlicoder.houtai.common.PageResult;
import com.binlicoder.houtai.entity.Teacher;
import com.binlicoder.houtai.mapper.TeacherMapper;
import com.binlicoder.houtai.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public PageResult<Teacher> findAll(String teacherNo, String name, Integer page, Integer pageSize) {
        page = page == null || page < 1 ? 1 : page;
        pageSize = pageSize == null || pageSize < 1 ? 10 : pageSize;
        Integer offset = (page - 1) * pageSize;
        return new PageResult<>(
            teacherMapper.findAll(teacherNo, name, offset, pageSize),
            teacherMapper.countAll(teacherNo, name),
            page,
            pageSize
        );
    }

    @Override
    public Teacher findById(Integer id) {
        return teacherMapper.findById(id);
    }

    @Override
    public Teacher findByUserId(Integer userId) {
        return teacherMapper.findByUserId(userId);
    }

    @Override
    public Teacher create(Teacher teacher) {
        teacherMapper.insert(teacher);
        return teacher;
    }

    @Override
    public Teacher update(Teacher teacher) {
        teacherMapper.update(teacher);
        return teacher;
    }

    @Override
    public void delete(Integer id) {
        teacherMapper.delete(id);
    }
}
