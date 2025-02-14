package com.iszion.api.cos.service.impl;

import com.iszion.api.cos.dao.CosDAO;
import com.iszion.api.cos.service.CosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("cosService")
public class CosServiceImpl implements CosService {

    private final CosDAO cosDAO;

    @Autowired
    public CosServiceImpl(CosDAO cosDAO) {
        this.cosDAO = cosDAO;
    }


    @Override
    public Object selectQryOne(String queryId, Object searchObj) throws Exception {
        return cosDAO.selectQryOne(queryId, searchObj);
    }

    @Override
    public List<?> selectQryList(String queryId, Object searchObj) throws Exception {
        return cosDAO.selectQryList(queryId, searchObj);
    }

    @Override
    public Object selectQryListCnt(String queryId, Object searchObj) throws Exception {
        return cosDAO.selectQryListCnt(queryId, searchObj);
    }

    @Override
    public int insertQry(String queryId, Object searchObj) throws Exception {
        return cosDAO.insertQry(queryId, searchObj);
    }

    @Override
    public int updateQry(String queryId, Object searchObj) throws Exception {
        return cosDAO.updateQry(queryId, searchObj);
    }

    @Override
    public int deleteQry(String queryId, Object searchObj) throws Exception {
        return cosDAO.deleteQry(queryId, searchObj);
    }
}
