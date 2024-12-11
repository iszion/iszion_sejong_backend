package com.iszion.api.sal.service.impl;

import com.iszion.api.sal.dao.SalDAO;
import com.iszion.api.sal.service.SalService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("salService")
public class SalServiceImpl implements SalService {

    @Resource(name = "salDAO")
    private SalDAO salDAO;

    @Override
    public Object selectQryOne(String queryId, Object searchObj) throws Exception {
        return salDAO.selectQryOne(queryId, searchObj);
    }

    @Override
    public List<?> selectQryList(String queryId, Object searchObj) throws Exception {
        return salDAO.selectQryList(queryId, searchObj);
    }

    @Override
    public Object selectQryListCnt(String queryId, Object searchObj) throws Exception {
        return salDAO.selectQryListCnt(queryId, searchObj);
    }

    @Override
    public int insertQry(String queryId, Object searchObj) throws Exception {
        return salDAO.insertQry(queryId, searchObj);
    }

    @Override
    public int updateQry(String queryId, Object searchObj) throws Exception {
        return salDAO.updateQry(queryId, searchObj);
    }

    @Override
    public int deleteQry(String queryId, Object searchObj) throws Exception {
        return salDAO.deleteQry(queryId, searchObj);
    }
}
