package com.binlicoder.houtai.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Course {
    private Integer id;
    private String courseName;
    private String courseCode;
    private Double credit;
    private Integer hours;
    private String courseType;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
