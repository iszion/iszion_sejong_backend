package com.iszion.api.com.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ComDBDAO {
  @Update("CREATE DATABASE `${dbName}` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
  void createDatabase(String dbName);

  void createTables(String dbName);
}
