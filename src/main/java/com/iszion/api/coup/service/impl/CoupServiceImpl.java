package com.iszion.api.coup.service.impl;

import com.iszion.api.coup.dao.CoupDAO;
import com.iszion.api.coup.service.CoupService;
import jakarta.annotation.Resource;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("coupService")
public class CoupServiceImpl implements CoupService {


    @Resource(name = "coupDAO")
    private CoupDAO coupDAO;

    @Override
    public Object selectQryOne(String queryId, Object searchObj) throws Exception {
        return coupDAO.selectQryOne(queryId, searchObj);
    }

    @Override
    public List<?> selectQryList(String queryId, Object searchObj) throws Exception {
        return coupDAO.selectQryList(queryId, searchObj);
    }

    @Override
    public Object selectQryListCnt(String queryId, Object searchObj) throws Exception {
        return coupDAO.selectQryListCnt(queryId, searchObj);
    }

    @Override
    public int insertQry(String queryId, Object searchObj) throws Exception {
        return coupDAO.insertQry(queryId, searchObj);
    }

    @Override
    public int updateQry(String queryId, Object searchObj) throws Exception {
        return coupDAO.updateQry(queryId, searchObj);
    }

    @Override
    public int deleteQry(String queryId, Object searchObj) throws Exception {
        return coupDAO.deleteQry(queryId, searchObj);
    }
}
