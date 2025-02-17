/*
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
//@MapperScan(value = {"com.iszion.api.sys.dao", "com.iszion.api.mst.dao", "com.iszion.api.sal.dao", "com.iszion.api.aux.dao", "com.iszion.api.fee.dao"}, sqlSessionTemplateRef="secondarySqlSessionFactory")
@EnableTransactionManagement
public class DatabaseSecondaryConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseSecondaryConfig.class);


  @Value("${spring.secondary.datasource.url}")
  private String url;

  @Value("${spring.secondary.datasource.username}")
  private String username;

  @Value("${spring.secondary.datasource.password}")
  private String password;

  @Value("${spring.secondary.datasource.driver-class-name}")
  private String driverClassName;

  @Bean(name="secondaryDataSource")
  public DataSource secondaryDataSource() {

    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setDriverClassName(driverClassName);

    return dataSource;
  }

  @Bean(name = "secondarySqlSessionFactory")
  public SqlSessionFactory secondarySqlSessionFactory(@Qualifier("secondaryDataSource") DataSource secondaryDataSource, ApplicationContext applicationContext) throws Exception {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(secondaryDataSource);
//    sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/secondary/*.xml"));
    sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/mapperSecondary/*.xml"));
    LOGGER.info("Initializing secondarySqlSessionFactory with {}", secondaryDataSource);


    // 추가: 로드된 매퍼 파일 경로 로그
    Resource[] mapperLocations = new PathMatchingResourcePatternResolver().getResources("classpath:mapper/mapperSecondary/*.xml");
    for (Resource resource : mapperLocations) {
      System.out.println("Loaded mapper secondary: " + resource.getFilename());
    }

    return sqlSessionFactoryBean.getObject();
    }

  @Bean(name = "secondarySqlSessionTemplate")
  public SqlSessionTemplate secondarySqlSessionTemplate(@Qualifier("secondarySqlSessionFactory")  SqlSessionFactory secondarySqlSessionFactory) throws Exception {
    LOGGER.info("Initializing secondarySqlSessionTemplate with {}", secondarySqlSessionFactory);
    return new SqlSessionTemplate(secondarySqlSessionFactory);
  }

  @Bean(name = "secondaryTransactionManager")
  public PlatformTransactionManager secondaryTransactionManager(@Qualifier("secondaryDataSource") DataSource secondaryDataSource) {
    return new DataSourceTransactionManager(secondaryDataSource);
  }


}
*/
