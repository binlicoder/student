package com.binlicoder.houtai.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Grade {
    private Integer id;
    private Integer studentId;
    private String studentNo;
    private String studentName;
    private Integer courseId;
    private String courseName;
    private Integer teacherId;
    private String teacherName;
    private String semester;
    private BigDecimal usualScore;
    private BigDecimal examScore;
    private BigDecimal totalScore;
    private String gradeLevel;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
