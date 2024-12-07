package com.iszion.api.mkt.service.impl;

import com.iszion.api.mkt.dao.MktDAO;
import com.iszion.api.mkt.service.MktService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mktService")
public class MktServiceImpl implements MktService {

    @Resource(name = "mktDAO")
    private MktDAO mktDAO;

    @Override
    public Object selectQryOne(String queryId, Object searchObj) throws Exception {
        return mktDAO.selectQryOne(queryId, searchObj);
    }

    @Override
    public List<?> selectQryList(String queryId, Object searchObj) throws Exception {
        return mktDAO.selectQryList(queryId, searchObj);
    }

    @Override
    public Object selectQryListCnt(String queryId, Object searchObj) throws Exception {
        return mktDAO.selectQryListCnt(queryId, searchObj);
    }

    @Override
    public int insertQry(String queryId, Object searchObj) throws Exception {
        return mktDAO.insertQry(queryId, searchObj);
    }

    @Override
    public int updateQry(String queryId, Object searchObj) throws Exception {
        return mktDAO.updateQry(queryId, searchObj);
    }

    @Override
    public int deleteQry(String queryId, Object searchObj) throws Exception {
        return mktDAO.deleteQry(queryId, searchObj);
    }
}
