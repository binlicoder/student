package com.binlicoder.houtai.service;

import com.binlicoder.houtai.common.PageResult;
import com.binlicoder.houtai.entity.Class;

public interface ClassService {
    PageResult<Class> findAll(String classCode, String className, Integer page, Integer pageSize);
    Class findById(Integer id);
    Class create(Class classEntity);
    Class update(Class classEntity);
    void delete(Integer id);
}
