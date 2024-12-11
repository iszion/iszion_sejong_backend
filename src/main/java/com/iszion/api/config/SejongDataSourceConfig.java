package com.iszion.api.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class SejongDataSourceConfig {

    @Bean(name = "sejongDataSource")
    public DataSource sejongDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:log4jdbc:mariadb://218.148.255.246:33062/db_sejong?allowMultiQueries=true")
                .driverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy")
                .username("iszion")
                .password("iszion1347#*")
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
