package com.binlicoder.houtai.mapper;

import com.binlicoder.houtai.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CourseMapper {
    List<Course> findAll(@Param("courseCode") String courseCode, @Param("courseName") String courseName,
                         @Param("offset") Integer offset, @Param("limit") Integer limit);
    int countAll(@Param("courseCode") String courseCode, @Param("courseName") String courseName);
    Course findById(@Param("id") Integer id);
    int insert(Course course);
    int update(Course course);
    int delete(@Param("id") Integer id);
}
