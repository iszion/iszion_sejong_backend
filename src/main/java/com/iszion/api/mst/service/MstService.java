package com.iszion.api.mst.service;

import java.util.List;

public interface MstService {
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
}