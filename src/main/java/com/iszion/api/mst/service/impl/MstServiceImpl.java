package com.iszion.api.mst.service.impl;

import com.iszion.api.mst.dao.MstDAO;
import com.iszion.api.mst.service.MstService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mstService")
public class MstServiceImpl implements MstService {

    @Resource(name = "mstDAO")
    private MstDAO mstDAO;

    @Override
    public Object selectQryOne(String queryId, Object searchObj) throws Exception {
        return mstDAO.selectQryOne(queryId, searchObj);
    }

    @Override
    public List<?> selectQryList(String queryId, Object searchObj) throws Exception {
        return mstDAO.selectQryList(queryId, searchObj);
    }

    @Override
    public Object selectQryListCnt(String queryId, Object searchObj) throws Exception {
        return mstDAO.selectQryListCnt(queryId, searchObj);
    }

    @Override
    public int insertQry(String queryId, Object searchObj) throws Exception {
        return mstDAO.insertQry(queryId, searchObj);
    }

    @Override
    public int updateQry(String queryId, Object searchObj) throws Exception {
        return mstDAO.updateQry(queryId, searchObj);
    }

    @Override
    public int deleteQry(String queryId, Object searchObj) throws Exception {
        return mstDAO.deleteQry(queryId, searchObj);
    }
}
