package com.iszion.api.aux.service.impl;

import com.iszion.api.aux.dao.AuxDAO;
import com.iszion.api.aux.service.AuxService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("auxService")
public class AuxServiceImpl implements AuxService {

    @Resource(name = "auxDAO")
    private AuxDAO auxDAO;

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
