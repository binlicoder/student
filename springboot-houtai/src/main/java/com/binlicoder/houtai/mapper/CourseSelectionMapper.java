package com.binlicoder.houtai.mapper;

import com.binlicoder.houtai.entity.CourseSelection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CourseSelectionMapper {
    List<CourseSelection> findAll(@Param("studentNo") String studentNo, @Param("courseId") Integer courseId,
                                   @Param("semester") String semester, @Param("offset") Integer offset,
                                   @Param("limit") Integer limit);
    int countAll(@Param("studentNo") String studentNo, @Param("courseId") Integer courseId,
                 @Param("semester") String semester);
    CourseSelection findById(@Param("id") Integer id);
    List<CourseSelection> findByStudentId(@Param("studentId") Integer studentId);
    int insert(CourseSelection courseSelection);
    int delete(@Param("id") Integer id);
}
