package com.iszion.api.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class SejongDataSourceConfig {

    @Value("${spring.datasource.secondary.url}")
    private String url;

    @Value("${spring.datasource.secondary.driver-class-name}")
    private String className;

    @Value("${spring.datasource.secondary.username}")
    private String userName;

    @Value("${spring.datasource.secondary.password}")
    private String passWord;

    @Bean(name = "sejongDataSource")
    public DataSource sejongDataSource() {
        return DataSourceBuilder.create()
                .url(url)
                .driverClassName(className)
                .username(userName)
                .password(passWord)
                .build();
    }

    @Bean(name = "sejongSqlSessionFactory")
    public SqlSessionFactory sejongSqlSessionFactory(@Qualifier("sejongDataSource") DataSource dataSource) throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));//xml파일의 위치, src/main/resources아래에 위치
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "sejongSqlSessionTemplate")
    public SqlSessionTemplate sejongSqlSessionTemplate(SqlSessionFactory SejongSqlSessionFactory) throws Exception {
        final SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(SejongSqlSessionFactory);
        return sqlSessionTemplate;
    }
}
