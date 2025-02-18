package com.iszion.api.com.service.impl;

import com.iszion.api.com.dao.ComDBDAO;
import com.iszion.api.com.service.ComDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service("comDBService")
public class ComDBServiceImpl implements ComDBService {
  private final ComDBDAO comDBDAO;

  public ComDBServiceImpl(ComDBDAO comDBDAO) {
    this.comDBDAO = comDBDAO;
  }

  @Override
  @Transactional
  public void createDatabase(String dbName) {
    if (!dbName.matches("^[a-zA-Z0-9_]+$")) {
      throw new IllegalArgumentException("Invalid database name.");
    }

    // 데이터베이스 생성
    comDBDAO.createDatabase(dbName);

    // 테이블 생성
    comDBDAO.createTables(dbName);
  }

  @Override
  public void createTables(String dbName) {

  }
}
