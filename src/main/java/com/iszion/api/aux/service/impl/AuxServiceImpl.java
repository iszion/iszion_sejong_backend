package com.iszion.api.aux.service.impl;

import com.iszion.api.aux.dao.AuxDAO;
import com.iszion.api.aux.service.AuxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("auxService")
public class AuxServiceImpl implements AuxService {

    private final AuxDAO auxDAO;

    @Autowired
    public AuxServiceImpl(AuxDAO auxDAO) {
        this.auxDAO = auxDAO;
    }


    @Override
    public Object selectQryOne(String queryId, Object searchObj) throws Exception {
        return auxDAO.selectQryOne(queryId, searchObj);
    }

    @Override
    public List<?> selectQryList(String queryId, Object searchObj) throws Exception {
        return auxDAO.selectQryList(queryId, searchObj);
    }

    @Override
    public Object selectQryListCnt(String queryId, Object searchObj) throws Exception {
        return auxDAO.selectQryListCnt(queryId, searchObj);
    }

    @Override
    public int insertQry(String queryId, Object searchObj) throws Exception {
        return auxDAO.insertQry(queryId, searchObj);
    }

    @Override
    public int updateQry(String queryId, Object searchObj) throws Exception {
        return auxDAO.updateQry(queryId, searchObj);
    }

    @Override
    public int deleteQry(String queryId, Object searchObj) throws Exception {
        return auxDAO.deleteQry(queryId, searchObj);
    }
}
