package com.binlicoder.houtai.mapper;

import com.binlicoder.houtai.entity.Class;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ClassMapper {
    List<Class> findAll(@Param("classCode") String classCode, @Param("className") String className,
                        @Param("offset") Integer offset, @Param("limit") Integer limit);
    int countAll(@Param("classCode") String classCode, @Param("className") String className);
    Class findById(@Param("id") Integer id);
    int insert(Class classEntity);
    int update(Class classEntity);
    int delete(@Param("id") Integer id);
}
