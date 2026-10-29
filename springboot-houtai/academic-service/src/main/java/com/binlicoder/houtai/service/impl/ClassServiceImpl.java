package com.binlicoder.houtai.service.impl;

import com.binlicoder.houtai.common.PageResult;
import com.binlicoder.houtai.entity.Class;
import com.binlicoder.houtai.mapper.ClassMapper;
import com.binlicoder.houtai.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassMapper classMapper;

    @Override
    public PageResult<Class> findAll(String classCode, String className, Integer page, Integer pageSize) {
        page = page == null || page < 1 ? 1 : page;
        pageSize = pageSize == null || pageSize < 1 ? 10 : pageSize;
        Integer offset = (page - 1) * pageSize;
        return new PageResult<>(
            classMapper.findAll(classCode, className, offset, pageSize),
            classMapper.countAll(classCode, className),
            page,
            pageSize
        );
    }

    @Override
    public Class findById(Integer id) {
        return classMapper.findById(id);
    }

    @Override
    public Class create(Class classEntity) {
        classMapper.insert(classEntity);
        return classEntity;
    }

    @Override
    public Class update(Class classEntity) {
        classMapper.update(classEntity);
        return classEntity;
    }

    @Override
    public void delete(Integer id) {
        classMapper.delete(id);
    }
}
