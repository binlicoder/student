package com.binlicoder.houtai.mapper;

import com.binlicoder.houtai.entity.Grade;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface GradeMapper {
    List<Grade> findAll(@Param("studentNo") String studentNo, @Param("courseId") Integer courseId,
                        @Param("semester") String semester, @Param("offset") Integer offset,
                        @Param("limit") Integer limit);
    int countAll(@Param("studentNo") String studentNo, @Param("courseId") Integer courseId,
                 @Param("semester") String semester);
    Grade findById(@Param("id") Integer id);
    List<Grade> findByStudentId(@Param("studentId") Integer studentId);
    int insert(Grade grade);
    int update(Grade grade);
    int delete(@Param("id") Integer id);
}
