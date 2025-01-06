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
@MapperScan(basePackages = "com.iszion.api.coup.dao", sqlSessionTemplateRef="db2SqlSessionFactory")
@EnableTransactionManagement
public class Database2Config {

  private static final Logger LOGGER = LoggerFactory.getLogger(Database2Config.class);


  @Value("${spring.db2.datasource.url}")
  private String url;

  @Value("${spring.db2.datasource.username}")
  private String username;

  @Value("${spring.db2.datasource.password}")
  private String password;

  @Value("${spring.db2.datasource.driver-class-name}")
  private String driverClassName;

  @Bean(name="db2DataSource")
  public DataSource db2DataSource() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setDriverClassName(driverClassName);

    logDatabaseConnection(dataSource);
    return dataSource;
  }

  @Bean(name = "db2SqlSessionFactory")
  public SqlSessionFactory db2SqlSessionFactory(@Qualifier("db2DataSource") DataSource db2DataSource, ApplicationContext applicationContext) throws Exception {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(db2DataSource);
//    sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/secondary/*.xml"));
    sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/secondary/*.xml"));
    LOGGER.info("Initializing db2SqlSessionFactory with {}", db2DataSource);


    // 추가: 로드된 매퍼 파일 경로 로그
    Resource[] mapperLocations = new PathMatchingResourcePatternResolver().getResources("classpath:mapper/secondary/*.xml");
    for (Resource resource : mapperLocations) {
      System.out.println("Loaded mapper db2: " + resource.getFilename());
    }

    return sqlSessionFactoryBean.getObject();
    }

  @Bean(name = "db2SqlSessionTemplate")
  public SqlSessionTemplate db2SqlSessionTemplate(@Qualifier("db2SqlSessionFactory")  SqlSessionFactory db2SqlSessionFactory) throws Exception {
    LOGGER.info("Initializing db2SqlSessionTemplate with {}", db2SqlSessionFactory);
    return new SqlSessionTemplate(db2SqlSessionFactory);
  }

  @Bean(name = "db2TransactionManager")
  public PlatformTransactionManager db2TransactionManager(@Qualifier("db2DataSource") DataSource db2DataSource) {
    return new DataSourceTransactionManager(db2DataSource);
  }

  /**
   * Logs the database connection status.
   */
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
