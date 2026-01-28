package com.example.boardproject.global.config;

import com.example.boardproject.global.typehandler.UuidTypeHandler;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.UUID;

@Configuration
@MapperScan(
        basePackages = "com.example.boardproject.domain.**.repository",
        annotationClass = Mapper.class
)
public class MyBatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        // Mapper XML 위치
        sessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath:mapper/**/*.xml"));

        // Type Aliases 패키지
        sessionFactory.setTypeAliasesPackage("com.example.boardproject.domain.**.entity");

        // TypeHandler 패키지 스캔
        sessionFactory.setTypeHandlersPackage("com.example.boardproject.global.typehandler");

        return sessionFactory.getObject();
    }
}