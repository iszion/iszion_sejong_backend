package com.iszion.api.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DynamicDataSourceConfig {

    @Value("${spring.secondary.datasource.url}")
    private String url;

    @Value("${spring.secondary.datasource.username}")
    private String defaultUsername;

    @Value("${spring.secondary.datasource.password}")
    private String defaultPassword;

    @Value("${spring.secondary.datasource.driver-class-name}")
    private String defaultDriverClassName;



    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();

        // 파라미터로 받은 DB 이름에 따라 동적으로 URL 생성
        //targetDataSources.put("secondary", createDataSource("secondary"));
        targetDataSources.put("db_sej", createDataSource("db_sej"));  // 추가된 db_secondary
        targetDataSources.put("db_isbook", createDataSource("db_isbook"));  // 추가된 db_secondary
        //targetDataSources.put("db_isbook", createDataSource("db_isbook"));


        DynamicRoutingDataSource dynamicDataSource = new DynamicRoutingDataSource();
        dynamicDataSource.setDefaultTargetDataSource(createDataSource("db_isbook"));  // 기본 DB는 secondary
        dynamicDataSource.setTargetDataSources(targetDataSources);
        System.out.println("database!!! : " + dynamicDataSource);
        return dynamicDataSource;
    }

    private DataSource createDataSource(String dbName) {
        HikariDataSource dataSource = new HikariDataSource();

        // DB명에 따라 JDBC URL 동적으로 설정
        String jdbcUrl = "jdbc:log4jdbc:mariadb://125.250.69.237:60004/" + dbName + "?allowMultiQueries=true";  // DB 이름에 따라 URL 설정
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(defaultUsername);
        dataSource.setPassword(defaultPassword);
        dataSource.setDriverClassName(defaultDriverClassName);
        return dataSource;
    }


    @Bean(name = "secondarySqlSessionFactory")
    public SqlSessionFactory secondarySqlSessionFactory(@Qualifier("dynamicDataSource") DataSource secondaryDataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(secondaryDataSource);
//    sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/secondary/*.xml"));
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/mapperSecondary/*.xml"));


        // 추가: 로드된 매퍼 파일 경로 로그
        Resource[] mapperLocations = new PathMatchingResourcePatternResolver().getResources("classpath:mapper/mapperSecondary/*.xml");
        for (Resource resource : mapperLocations) {
            System.out.println("Loaded mapper secondary: " + resource.getFilename());
        }

        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "secondarySqlSessionTemplate")
    public SqlSessionTemplate secondarySqlSessionTemplate(@Qualifier("secondarySqlSessionFactory")  SqlSessionFactory secondarySqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(secondarySqlSessionFactory);
    }

    @Bean(name = "secondaryTransactionManager")
    public PlatformTransactionManager secondaryTransactionManager(@Qualifier("dynamicDataSource") DataSource secondaryDataSource) {
        return new DataSourceTransactionManager(secondaryDataSource);
    }


}

