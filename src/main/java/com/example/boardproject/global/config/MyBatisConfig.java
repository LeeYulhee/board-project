package com.example.boardproject.global.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(
        basePackages = "com.example.boardproject.domain.**.repository",
        annotationClass = Mapper.class
)
public class MyBatisConfig {
}