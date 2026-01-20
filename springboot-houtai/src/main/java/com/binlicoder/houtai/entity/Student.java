package com.binlicoder.houtai.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Student {
    private Integer id;
    private Integer userId;
    private String studentNo;
    private Integer classId;
    private String className;
    private String gender;
    private LocalDate birthday;
    private String phone;
    private String email;
    private String address;
    private LocalDate enrollmentDate;
    private String status;
    private String realName;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
