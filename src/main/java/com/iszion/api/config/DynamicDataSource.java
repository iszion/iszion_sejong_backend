package com.iszion.api.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component("dynamicDataSource")
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();
    private final Map<Object, Object> dataSourceCache = new ConcurrentHashMap<>();
    private final Object lock = new Object();

    public static void setCurrentDb(String dbKey) {
        System.out.println("✅ setCurrentDb() - 변경할 DB: " + dbKey);
        contextHolder.set(dbKey);
    }

    public static void clear() {
        System.out.println("❌ clear() - DB 초기화");
        contextHolder.remove();
    }

    @Override
    public Object determineCurrentLookupKey() {
        String currentDb = contextHolder.get(); // 현재 저장된 DB Key
        return contextHolder.get();
    }

    public DataSource getOrCreateDataSource(String dbName) {
        if (dataSourceCache != null) {
            dataSourceCache.clear();  // 캐시된 데이터 소스들을 비워줍니다.
            System.out.println("DataSourceCache cleared");
        }
        synchronized (lock) {
            // 먼저 현재 DB 설정을 변경
            setCurrentDb(dbName);

            // 새 DB 데이터소스 생성
            if (!dataSourceCache.containsKey(dbName)) {
                DataSource newDataSource = createNewDataSource(dbName);
                dataSourceCache.put(dbName, newDataSource);
                setTargetDataSources(new HashMap<>(dataSourceCache)); // 새로운 데이터소스 반영
                setDefaultTargetDataSource(newDataSource); // 기본 데이터소스도 업데이트
                afterPropertiesSet(); // Spring에게 설정 변경 알림
            }
            return (DataSource) dataSourceCache.get(dbName);
        }
    }

    private DataSource createNewDataSource(String dbName) {
        String jdbcUrl = "jdbc:log4jdbc:mariadb://125.250.69.237:60004/" + dbName + "?allowMultiQueries=true";
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername("iszion");
        config.setPassword("iszion1347#*");
        config.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
        return new HikariDataSource(config);
    }

    public static Object getCurrentDb() {
        String currentDb = contextHolder.get();
        return currentDb;
    }
}



