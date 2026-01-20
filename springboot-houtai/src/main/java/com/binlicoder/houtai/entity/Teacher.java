package com.binlicoder.houtai.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Teacher {
    private Integer id;
    private Integer userId;
    private String teacherNo;
    private String gender;
    private String phone;
    private String email;
    private String title;
    private String department;
    private String realName;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
