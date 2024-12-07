package com.iszion.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = {MultipartAutoConfiguration.class})
@EnableAsync
public class IszionErpBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(IszionErpBackendApplication.class, args);
    }

}
