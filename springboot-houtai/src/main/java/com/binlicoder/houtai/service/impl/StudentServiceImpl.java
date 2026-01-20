package com.binlicoder.houtai.service.impl;

import com.binlicoder.houtai.common.PageResult;
import com.binlicoder.houtai.entity.Student;
import com.binlicoder.houtai.mapper.StudentMapper;
import com.binlicoder.houtai.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public PageResult<Student> findAll(String studentNo, String name, Integer classId, Integer page, Integer pageSize) {
        page = page == null || page < 1 ? 1 : page;
        pageSize = pageSize == null || pageSize < 1 ? 10 : pageSize;
        Integer offset = (page - 1) * pageSize;
        return new PageResult<>(
            studentMapper.findAll(studentNo, name, classId, offset, pageSize),
            studentMapper.countAll(studentNo, name, classId),
            page,
            pageSize
        );
    }

    @Override
    public Student findById(Integer id) {
        return studentMapper.findById(id);
    }

    @Override
    public Student findByUserId(Integer userId) {
        return studentMapper.findByUserId(userId);
    }

    @Override
    public Student create(Student student) {
        studentMapper.insert(student);
        return student;
    }

    @Override
    public Student update(Student student) {
        studentMapper.update(student);
        return student;
    }

    @Override
    public void delete(Integer id) {
        studentMapper.delete(id);
    }
}
