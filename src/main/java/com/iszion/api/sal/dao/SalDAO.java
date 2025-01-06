package com.iszion.api.sal.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("salDAO")
public class SalDAO {

    @Autowired
    @Qualifier("db1SqlSessionTemplate")
    private SqlSessionTemplate db1SqlSessionTemplate;

    /**
     * 조회 한건
     * @param
     * @return
     * @exception Exception
     */
    public Object selectQryOne(String queryId,Object searchObj) throws Exception {
        return db1SqlSessionTemplate.selectOne(queryId, searchObj);
    }
    /**
     * 조회리스트
     * @param
     * @return
     * @exception Exception
     */
    public List<?> selectQryList(String queryId, Object searchObj) throws Exception {
        return db1SqlSessionTemplate.selectList(queryId, searchObj);
    }
    /**
     * 조회리스트
     * @param
     * @return
     * @exception Exception
     */
    public Object selectQryListCnt(String queryId,Object searchObj) throws Exception {
        return db1SqlSessionTemplate.selectOne(queryId, searchObj);
    }
    /**
     * 입력
     * @param
     * @return
     * @exception Exception
     */
    public int insertQry(String queryId,Object searchObj) throws Exception {
        return db1SqlSessionTemplate.insert(queryId, searchObj);
    }
    /**
     * 수정
     * @param
     * @return
     * @exception Exception
     */
    public int updateQry(String queryId,Object searchObj) throws Exception {
        return db1SqlSessionTemplate.update(queryId, searchObj);
    }
    /**
     *  삭제
     * @param
     * @return
     * @exception Exception
     */
    public int deleteQry(String queryId,Object searchObj) throws Exception {
        return db1SqlSessionTemplate.delete(queryId, searchObj);
    }
}
