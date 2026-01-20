package com.binlicoder.houtai.mapper;

import com.binlicoder.houtai.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface TeacherMapper {
    List<Teacher> findAll(@Param("teacherNo") String teacherNo, @Param("name") String name,
                          @Param("offset") Integer offset, @Param("limit") Integer limit);
    int countAll(@Param("teacherNo") String teacherNo, @Param("name") String name);
    Teacher findById(@Param("id") Integer id);
    Teacher findByUserId(@Param("userId") Integer userId);
    int insert(Teacher teacher);
    int update(Teacher teacher);
    int delete(@Param("id") Integer id);
}
