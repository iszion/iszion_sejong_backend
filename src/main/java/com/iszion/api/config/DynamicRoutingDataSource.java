package com.iszion.api.config;

import com.iszion.api.aux.controller.AuxController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<String> currentDataSource = new ThreadLocal<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(AuxController.class);

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSource = currentDataSource.get();
        LOGGER.info("Current data source: {}", dataSource); // 로그로 확인
        return currentDataSource.get();  // 현재 선택된 DB 이름 반환
    }

    public static String getCurrentDatabaseName() {
        return currentDataSource.get();  // 현재 설정된 DB 이름 반환
    }

    public static void setDataSource(String dataSource) {
        currentDataSource.set(dataSource);
    }

    public static void clearDataSource() {
        currentDataSource.remove();
    }
}


