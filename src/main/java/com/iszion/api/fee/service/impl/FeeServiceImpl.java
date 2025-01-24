package com.iszion.api.fee.service.impl;

import com.iszion.api.fee.dao.FeeDAO;
import com.iszion.api.fee.service.FeeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("feeService")
public class FeeServiceImpl implements FeeService {

    @Resource(name = "feeDAO")
    private FeeDAO feeDAO;

    @Override
    public Object selectQryOne(String queryId, Object searchObj) throws Exception {
        return feeDAO.selectQryOne(queryId, searchObj);
    }

    @Override
    public List<?> selectQryList(String queryId, Object searchObj) throws Exception {
        return feeDAO.selectQryList(queryId, searchObj);
    }

    @Override
    public Object selectQryListCnt(String queryId, Object searchObj) throws Exception {
        return feeDAO.selectQryListCnt(queryId, searchObj);
    }

    @Override
    public int insertQry(String queryId, Object searchObj) throws Exception {
        return feeDAO.insertQry(queryId, searchObj);
    }

    @Override
    public int updateQry(String queryId, Object searchObj) throws Exception {
        return feeDAO.updateQry(queryId, searchObj);
    }

    @Override
    public int deleteQry(String queryId, Object searchObj) throws Exception {
        return feeDAO.deleteQry(queryId, searchObj);
    }
}
