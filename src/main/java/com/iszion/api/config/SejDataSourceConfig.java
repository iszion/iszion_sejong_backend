package com.iszion.api.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
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

    @Bean
    @Primary
    public DataSource sejDataSource() {

        return DataSourceBuilder.create()
                .url("jdbc:log4jdbc:mariadb://125.250.69.237:60004/db_sej?allowMultiQueries=true")
                .driverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy") // 드라이버 클래스명을 명시적으로 지정
                .username("iszion")
                .password("iszion1347#*")
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
