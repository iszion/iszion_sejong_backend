package com.iszion.api.com.service;

import io.lettuce.core.dynamic.annotation.Param;

public interface ComDBService {
  void createDatabase(String dbName);
  void createTables(@Param("dbName") String dbName);
}