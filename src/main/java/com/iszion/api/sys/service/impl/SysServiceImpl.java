package com.iszion.api.sys.service.impl;


import com.iszion.api.sys.dao.SysDAO;
import com.iszion.api.sys.service.SysService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


@Service
public class SysServiceImpl implements SysService {

    @Resource(name = "sysDAO")
    private SysDAO sysDAO;

    @Override
    public Object selectQryOne(String queryId, Object searchObj) throws Exception {
        return sysDAO.selectQryOne(queryId, searchObj);
    }

    @Override
    public HashMap<String, Object> selectQryOne1(String queryId, Object searchObj) throws Exception {
        return sysDAO.selectQryOne1(queryId, searchObj);
    }

    @Override
    public List<?> selectQryList(String queryId, Object searchObj) throws Exception {
        return sysDAO.selectQryList(queryId, searchObj);
    }

    @Override
    public Object selectQryListCnt(String queryId, Object searchObj) throws Exception {
        return sysDAO.selectQryListCnt(queryId, searchObj);
    }

    @Override
    public int insertQry(String queryId, Object searchObj) throws Exception {
        return sysDAO.insertQry(queryId, searchObj);
    }

    @Override
    public int updateQry(String queryId, Object searchObj) throws Exception {
        return sysDAO.updateQry(queryId, searchObj);
    }

    @Override
    public int deleteQry(String queryId, Object searchObj) throws Exception {
        return sysDAO.deleteQry(queryId, searchObj);
    }

    @Override
    public List<?> selectQryList1(String queryId, Object searchObj, String dbType) throws Exception {
        return sysDAO.selectQryList1(queryId, searchObj, dbType);
    }
}
