package com.iszion.api.com.service.impl;

import com.iszion.api.com.dao.ComDAO;
import com.iszion.api.com.service.ComService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service("comService")
public class ComServiceImpl implements ComService {

    private final ComDAO comDAO;

    @Autowired
    public ComServiceImpl(ComDAO comDAO) {
        this.comDAO = comDAO;
    }


    @Override
    public Object selectQryOne(String queryId, Object searchObj) throws Exception {
        return comDAO.selectQryOne(queryId, searchObj);
    }

    @Override
    public HashMap<String, Object> selectQryOne1(String queryId, Object searchObj) throws Exception {
        return comDAO.selectQryOne1(queryId, searchObj);
    }


    @Override
    public List<?> selectQryList(String queryId, Object searchObj) throws Exception {
        return comDAO.selectQryList(queryId, searchObj);
    }

    @Override
    public Object selectQryListCnt(String queryId, Object searchObj) throws Exception {
        return comDAO.selectQryListCnt(queryId, searchObj);
    }

    @Override
    public int insertQry(String queryId, Object searchObj) throws Exception {
        return comDAO.insertQry(queryId, searchObj);
    }

    @Override
    public int updateQry(String queryId, Object searchObj) throws Exception {
        return comDAO.updateQry(queryId, searchObj);
    }

    @Override
    public int deleteQry(String queryId, Object searchObj) throws Exception {
        return comDAO.deleteQry(queryId, searchObj);
    }
}
