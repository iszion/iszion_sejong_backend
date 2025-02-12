package com.iszion.api.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository("sysDAO")
public class SysDAO {

    @Autowired
    @Qualifier("secondarySqlSessionTemplate")
    private SqlSessionTemplate secondarySqlSessionTemplate;


    /**
     * 조회 한건
     * @param
     * @return
     * @exception Exception
     */
    public Object selectQryOne(String queryId,Object searchObj) throws Exception {
        return secondarySqlSessionTemplate.selectOne(queryId, searchObj);
    }

    /**
     * 조회 한건
     * @param
     * @return
     * @exception Exception
     */
    public HashMap<String, Object> selectQryOne1(String queryId, Object searchObj) throws Exception {
        return secondarySqlSessionTemplate.selectOne(queryId, searchObj);
    }
    /**
     * 조회리스트
     * @param
     * @return
     * @exception Exception
     */
    public List<?> selectQryList(String queryId, Object searchObj) throws Exception {
        return secondarySqlSessionTemplate.selectList(queryId, searchObj);
    }
    /**
     * 조회리스트
     * @param
     * @return
     * @exception Exception
     */
    public Object selectQryListCnt(String queryId,Object searchObj) throws Exception {
        return secondarySqlSessionTemplate.selectOne(queryId, searchObj);
    }
    /**
     * 입력
     * @param
     * @return
     * @exception Exception
     */
    public int insertQry(String queryId,Object searchObj) throws Exception {
        return secondarySqlSessionTemplate.insert(queryId, searchObj);
    }
    /**
     * 수정
     * @param
     * @return
     * @exception Exception
     */
    public int updateQry(String queryId,Object searchObj) throws Exception {
        return secondarySqlSessionTemplate.update(queryId, searchObj);
    }
    /**
     *  삭제
     * @param
     * @return
     * @exception Exception
     */
    public int deleteQry(String queryId,Object searchObj) throws Exception {
        return secondarySqlSessionTemplate.delete(queryId, searchObj);
    }

}
