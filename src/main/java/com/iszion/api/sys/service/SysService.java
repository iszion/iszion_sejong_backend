package com.iszion.api.sys.service;

import java.util.HashMap;
import java.util.List;

public interface SysService {
    /**
     *
     * @param
     * @return
     * @exception Exception
     */
    Object selectQryOne(String queryId, Object searchObj) throws Exception;

    /**
     *
     * @param
     * @return
     * @exception Exception
     */
    HashMap<String, Object> selectQryOne1(String queryId, Object searchObj) throws Exception;
    /**
     *
     * @param
     * @return
     * @exception Exception
     */
    List<?> selectQryList(String queryId, Object searchObj) throws Exception;
    /**
     *
     * @param
     * @return
     * @exception Exception
     */
    Object selectQryListCnt(String queryId,Object searchObj) throws Exception;
    /**
     *
     * @param
     * @return
     * @exception Exception
     */
    int insertQry(String queryId,Object searchObj) throws Exception;
    /**
     *
     * @param
     * @return
     * @exception Exception
     */
    int updateQry(String queryId,Object searchObj) throws Exception;

    /**
     *
     * @param
     * @return
     * @exception Exception
     */
    int deleteQry(String queryId,Object searchObj) throws Exception;

    List<?> selectQryList1(String queryId, Object searchObj, String dbType) throws Exception;
}
