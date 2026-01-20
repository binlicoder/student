package com.binlicoder.houtai.mapper;

import com.binlicoder.houtai.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface StudentMapper {
    List<Student> findAll(@Param("studentNo") String studentNo, @Param("name") String name, 
                          @Param("classId") Integer classId, @Param("offset") Integer offset, 
                          @Param("limit") Integer limit);
    int countAll(@Param("studentNo") String studentNo, @Param("name") String name, 
                 @Param("classId") Integer classId);
    Student findById(@Param("id") Integer id);
    Student findByUserId(@Param("userId") Integer userId);
    int insert(Student student);
    int update(Student student);
    int delete(@Param("id") Integer id);
}
