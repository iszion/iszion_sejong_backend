package com.iszion.api.mst.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("mstDAO")
public class MstDAO {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 조회 한건
     * @param
     * @return
     * @exception Exception
     */
    public Object selectQryOne(String queryId,Object searchObj) throws Exception {
        return sqlSessionTemplate.selectOne(queryId, searchObj);
    }
    /**
     * 조회리스트
     * @param
     * @return
     * @exception Exception
     */
    public List<?> selectQryList(String queryId, Object searchObj) throws Exception {
        return sqlSessionTemplate.selectList(queryId, searchObj);
    }
    /**
     * 조회리스트
     * @param
     * @return
     * @exception Exception
     */
    public Object selectQryListCnt(String queryId,Object searchObj) throws Exception {
        return sqlSessionTemplate.selectOne(queryId, searchObj);
    }
    /**
     * 입력
     * @param
     * @return
     * @exception Exception
     */
    public int insertQry(String queryId,Object searchObj) throws Exception {
        return sqlSessionTemplate.insert(queryId, searchObj);
    }
    /**
     * 수정
     * @param
     * @return
     * @exception Exception
     */
    public int updateQry(String queryId,Object searchObj) throws Exception {
        return sqlSessionTemplate.update(queryId, searchObj);
    }
    /**
     *  삭제
     * @param
     * @return
     * @exception Exception
     */
    public int deleteQry(String queryId,Object searchObj) throws Exception {
        return sqlSessionTemplate.delete(queryId, searchObj);
    }
}
