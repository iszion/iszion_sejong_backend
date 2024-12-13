package com.iszion.api.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class SejDataSourceConfig {

    @Value("${spring.datasource.primary.url}")
    private String url;

    @Value("${spring.datasource.primary.driver-class-name}")
    private String className;

    @Value("${spring.datasource.primary.username}")
    private String userName;

    @Value("${spring.datasource.primary.password}")
    private String passWord;

    @Bean
    @Primary
    public DataSource sejDataSource() {

        return DataSourceBuilder.create()
                .url(url)
                .driverClassName(className) // 드라이버 클래스명을 명시적으로 지정
                .username(userName)
                .password(passWord)
                .build();
    }

    @Bean
    @Primary
    public SqlSessionFactory sejSqlSessionFactory(DataSource sejDataSource, ApplicationContext applicationContext) throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(sejDataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/*.xml"));//xml파일의 위치, src/main/resources아래에 위치
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "sejSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sejSqlSessionTemplate(SqlSessionFactory sejSqlSessionFactory) throws Exception {
        final SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sejSqlSessionFactory);
        return sqlSessionTemplate;
    }

}
