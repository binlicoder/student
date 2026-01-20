package com.binlicoder.houtai.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CourseSelection {
    private Integer id;
    private Integer studentId;
    private String studentNo;
    private String studentName;
    private Integer courseId;
    private String courseName;
    private Integer teacherId;
    private String teacherName;
    private String semester;
    private String status;
    private LocalDateTime selectionTime;
}
