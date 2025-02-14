package com.iszion.api.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
@MapperScan(value = {"com.iszion.api.auth.mapper", "com.iszion.api.com.dao"}, sqlSessionFactoryRef = "primarySqlSessionFactory")
@EnableTransactionManagement
public class DatabasePrimaryConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(DatabasePrimaryConfig.class);

  @Value("${spring.primary.datasource.url}")
  private String url;

  @Value("${spring.primary.datasource.username}")
  private String username;

  @Value("${spring.primary.datasource.password}")
  private String password;

  @Value("${spring.primary.datasource.driver-class-name}")
  private String driverClassName;

  @Primary
  @Bean(name = "primaryDataSource")
  public DataSource primaryDataSource() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setDriverClassName(driverClassName);

    return dataSource;
  }

  @Primary
  @Bean(name = "primarySqlSessionFactory")
  public SqlSessionFactory primarySqlSessionFactory(@Qualifier("primaryDataSource") DataSource primaryDataSource, ApplicationContext applicationContext) throws Exception {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(primaryDataSource);
//    sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/primary/*.xml"));
    sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/mapperPrimary/*.xml"));
    LOGGER.info("Initializing primarySqlSessionFactory with {}", primaryDataSource);

    // 추가: 로드된 매퍼 파일 경로 로그
    Resource[] mapperLocations = new PathMatchingResourcePatternResolver().getResources("classpath:mapper/mapperPrimary/*.xml");
    for (Resource resource : mapperLocations) {
      System.out.println("Loaded mapper primary: " + resource.getFilename());
    }
    return sqlSessionFactoryBean.getObject();
  }

  @Primary
  @Bean(name = "primarySqlSessionTemplate")
  public SqlSessionTemplate primarySqlSessionTemplate(@Qualifier("primarySqlSessionFactory") SqlSessionFactory primarySqlSessionFactory) throws Exception {
    LOGGER.info("Initializing primarySqlSessionTemplate with {}", primarySqlSessionFactory);
    return new SqlSessionTemplate(primarySqlSessionFactory);
  }

  @Primary
  @Bean(name = "primaryTransactionManager")
  public PlatformTransactionManager primaryTransactionManager(@Qualifier("primaryDataSource") DataSource primaryDataSource) {
    return new DataSourceTransactionManager(primaryDataSource);
  }

}