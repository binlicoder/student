package com.binlicoder.houtai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.binlicoder.houtai.mapper")
public class SpringbootHoutaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootHoutaiApplication.class, args);
    }

}
