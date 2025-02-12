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
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
//@MapperScan(basePackages = "com.iszion.api.coup.dao", sqlSessionTemplateRef="coupSqlSessionFactory")
@EnableTransactionManagement
public class DatabaseCoupConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseCoupConfig.class);


  @Value("${spring.coup.datasource.url}")
  private String url;

  @Value("${spring.coup.datasource.username}")
  private String username;

  @Value("${spring.coup.datasource.password}")
  private String password;

  @Value("${spring.coup.datasource.driver-class-name}")
  private String driverClassName;

  @Bean(name="coupDataSource")
  public DataSource coupDataSource() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setDriverClassName(driverClassName);

    return dataSource;
  }

  @Bean(name = "coupSqlSessionFactory")
  public SqlSessionFactory coupSqlSessionFactory(@Qualifier("coupDataSource") DataSource coupDataSource, ApplicationContext applicationContext) throws Exception {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(coupDataSource);
//    sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/secondary/*.xml"));
    sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/mapperCoup/*.xml"));
    LOGGER.info("Initializing coupSqlSessionFactory with {}", coupDataSource);


    // 추가: 로드된 매퍼 파일 경로 로그
    Resource[] mapperLocations = new PathMatchingResourcePatternResolver().getResources("classpath:mapper/mapperCoup/*.xml");
    for (Resource resource : mapperLocations) {
      System.out.println("Loaded mapper coup: " + resource.getFilename());
    }

    return sqlSessionFactoryBean.getObject();
    }

  @Bean(name = "coupSqlSessionTemplate")
  public SqlSessionTemplate coupSqlSessionTemplate(@Qualifier("coupSqlSessionFactory")  SqlSessionFactory coupSqlSessionFactory) throws Exception {
    LOGGER.info("Initializing coupSqlSessionTemplate with {}", coupSqlSessionFactory);
    return new SqlSessionTemplate(coupSqlSessionFactory);
  }

  @Bean(name = "coupTransactionManager")
  public PlatformTransactionManager coupTransactionManager(@Qualifier("coupDataSource") DataSource coupDataSource) {
    return new DataSourceTransactionManager(coupDataSource);
  }

}
