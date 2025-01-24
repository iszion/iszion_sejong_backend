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
import java.sql.Connection;
import java.sql.DatabaseMetaData;

@Configuration
@PropertySource("classpath:application.properties")
@MapperScan(value = {"com.iszion.api.auth.mapper", "com.iszion.api.sys.dao", "com.iszion.api.mst.dao", "com.iszion.api.sal.dao", "com.iszion.api.aux.dao", "com.iszion.api.fee.dao"}, sqlSessionFactoryRef = "db1SqlSessionFactory")
@EnableTransactionManagement
public class Database1Config {

  private static final Logger LOGGER = LoggerFactory.getLogger(Database1Config.class);

  @Value("${spring.db1.datasource.url}")
  private String url;

  @Value("${spring.db1.datasource.username}")
  private String username;

  @Value("${spring.db1.datasource.password}")
  private String password;

  @Value("${spring.db1.datasource.driver-class-name}")
  private String driverClassName;

  @Primary
  @Bean(name = "db1DataSource")
  public DataSource db1DataSource() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setDriverClassName(driverClassName);

    logDatabaseConnection(dataSource);
    return dataSource;
  }

  @Primary
  @Bean(name = "db1SqlSessionFactory")
  public SqlSessionFactory db1SqlSessionFactory(@Qualifier("db1DataSource") DataSource db1DataSource, ApplicationContext applicationContext) throws Exception {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(db1DataSource);
//    sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/primary/*.xml"));
    sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/primary/*.xml"));
    LOGGER.info("Initializing db1SqlSessionFactory with {}", db1DataSource);

    // 추가: 로드된 매퍼 파일 경로 로그
    Resource[] mapperLocations = new PathMatchingResourcePatternResolver().getResources("classpath:mapper/primary/*.xml");
    for (Resource resource : mapperLocations) {
      System.out.println("Loaded mapper db1: " + resource.getFilename());
    }
    return sqlSessionFactoryBean.getObject();
  }

  @Primary
  @Bean(name = "db1SqlSessionTemplate")
  public SqlSessionTemplate db1SqlSessionTemplate(@Qualifier("db1SqlSessionFactory") SqlSessionFactory db1SqlSessionFactory) throws Exception {
    LOGGER.info("Initializing db1SqlSessionTemplate with {}", db1SqlSessionFactory);
    return new SqlSessionTemplate(db1SqlSessionFactory);
  }

  @Primary
  @Bean(name = "db1TransactionManager")
  public PlatformTransactionManager db1TransactionManager(@Qualifier("db1DataSource") DataSource db1DataSource) {
    return new DataSourceTransactionManager(db1DataSource);
  }

  /**
   * Logs the database connection status.
   */
  @Primary
  private void logDatabaseConnection(DataSource dataSource) {
    try (Connection connection = dataSource.getConnection()) {
      if (connection != null) {
        DatabaseMetaData metaData = connection.getMetaData();
        LOGGER.info("Connected to database: {}", metaData.getURL());
        LOGGER.info("Database product name: {}", metaData.getDatabaseProductName());
        LOGGER.info("Database product version: {}", metaData.getDatabaseProductVersion());
      } else {
        LOGGER.warn("Failed to establish a database connection.");
      }
    } catch (Exception e) {
      LOGGER.error("Error while checking database connection status", e);
    }
  }

}