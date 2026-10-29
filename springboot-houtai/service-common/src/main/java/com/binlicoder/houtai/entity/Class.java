package com.binlicoder.houtai.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Class {
    private Integer id;
    private String className;
    private String classCode;
    private String grade;
    private String major;
    private Integer headTeacherId;
    private String headTeacherName;
    private Integer classSize;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
