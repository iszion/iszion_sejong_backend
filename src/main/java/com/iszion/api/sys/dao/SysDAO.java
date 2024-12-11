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
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    @Qualifier(value = "sejongSqlSessionTemplate") // 같은 bean이 2개 이상 존재할 시 어떤 bean을 사용할 건지 지정해야함.
    private SqlSessionTemplate sqlSession2;

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
     * 조회 한건
     * @param
     * @return
     * @exception Exception
     */
    public HashMap<String, Object> selectQryOne1(String queryId, Object searchObj) throws Exception {
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

    public List<?> selectQryList1(String queryId, Object searchObj, String dbType) throws Exception {
        return sqlSessionTemplate.selectList(queryId, null);

    }
}
