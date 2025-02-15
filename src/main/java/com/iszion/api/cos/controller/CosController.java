package com.iszion.api.cos.controller;

import com.iszion.api.comn.DataRequestUtil;
import com.iszion.api.cos.service.CosService;
import com.iszion.api.comn.JsonUtils;
import com.iszion.api.comn.RequestUtil;
import com.iszion.api.config.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cos")
public class CosController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CosController.class);
    private final CosService cosService;
    private final JwtTokenProvider jwtTokenProvider;
//    private final PlatformTransactionManager transactionManager;  // 트랜잭션 매니저


    @Qualifier("secondaryTransactionManager")  // Use @Qualifier to inject the specific transaction manager for primary
    private final PlatformTransactionManager secondaryTransactionManager;


    @Value("${spring.secondary.datasource.url}")
    private String DB_URL;
    @Value("${spring.secondary.datasource.username}")
    private String DB_USERID;
    @Value("${spring.secondary.datasource.password}")
    private String DB_PASSWORD;

    /* *******************************************************************************
     ** 손익분기 도서 부수지정
     ** ******************************************************************************* */
    @PostMapping("/cos1010_list")
    public String cos1010_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
        Object result;

        String jsonDataRtn = "";
        RequestUtil requestUtil = new RequestUtil();
        JsonUtils jsonUtils = new JsonUtils();

        String jsonData = requestUtil.getBody(request);

        Map<String, Object> reqParam = new HashMap<String, Object>();
        if (!jsonData.isEmpty()) {
            reqParam = jsonUtils.jsonStringToMap(jsonData);
        }
        try {
            result = cosService.selectQryList("cos1010_list", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
//            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }


    @PostMapping("/cos1010_save")
    public String cos1010_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = secondaryTransactionManager.getTransaction(def);
        // 트랜잭션 정의 끝


        String jsonDataRtn = "";
        String rtn = "0";
        String rtnMsg = "";
        List<?> divde = null;
        Map<String, Object> map = new HashMap();
        JsonUtils jsonUtil = new JsonUtils();
        DataRequestUtil reqUtil = new DataRequestUtil();

        String jsonData = reqUtil.getBody(request);
        try {
            Map<String, Object> mapDivde = jsonUtil.jsonStringToMap(jsonData);
            Map divde_N1 =  (Map) mapDivde.get("no1");
            if (divde_N1 != null) {
                List divde_I = (List) divde_N1.get("I");
                List divde_U = (List) divde_N1.get("U");
                List divde_D = (List) divde_N1.get("D");

                if (!divde_I.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    int rtnI = cosService.insertQry("cos1010_insert", param);
                    if(rtnI > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                    divde = divde_I;
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = cosService.updateQry("cos1010_update", param);
                    if(rtnU > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                    divde = divde_U;
                }

                if (!divde_D.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_D);
                    param.put("userId", userInfo.getName());
                    int rtnD = cosService.deleteQry("cos1010_delete", param);
                    if(rtnD > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                    divde = divde_D;
                }
            }
            if(rtn == "0") {
                rtnMsg = "정상 처리되었습니다.";
                secondaryTransactionManager.commit(status);
            } else {
                rtnMsg = "비정상 처리되었습니다.";
                secondaryTransactionManager.rollback(status);
            }
        } catch (Exception e) {
            secondaryTransactionManager.rollback(status);
            rtn = "3";
            if (e.getCause() instanceof SQLException sqlException) {
                rtnMsg = "처리실패 : " + sqlException.getMessage();  // Get the specific error message from SQLException
            } else {
                rtnMsg = "예상치 못한 오류가 발생했습니다.";
            }
        }
        map.put("rtn", rtn);
        map.put("rtnMsg", rtnMsg);
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }


    /* *******************************************************************************
     ** 제작비용 일괄등록
     ** ******************************************************************************* */
    @PostMapping("/cos1020_list")
    public String cos1020_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
        Object result;

        String jsonDataRtn = "";
        RequestUtil requestUtil = new RequestUtil();
        JsonUtils jsonUtils = new JsonUtils();

        String jsonData = requestUtil.getBody(request);

        Map<String, Object> reqParam = new HashMap<String, Object>();
        if (!jsonData.isEmpty()) {
            reqParam = jsonUtils.jsonStringToMap(jsonData);
        }
        try {
            result = cosService.selectQryList("cos1020_list", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
//            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }


    @PostMapping("/cos1020_save")
    public String cos1020_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = secondaryTransactionManager.getTransaction(def);
        // 트랜잭션 정의 끝


        String jsonDataRtn = "";
        String rtn = "0";
        String rtnMsg = "";
        List<?> divde = null;
        Map<String, Object> map = new HashMap();
        JsonUtils jsonUtil = new JsonUtils();
        DataRequestUtil reqUtil = new DataRequestUtil();

        String jsonData = reqUtil.getBody(request);
        try {
            Map<String, Object> mapDivde = jsonUtil.jsonStringToMap(jsonData);
            Map divde_N1 =  (Map) mapDivde.get("no1");
            if (divde_N1 != null) {
                List divde_I = (List) divde_N1.get("I");
                List divde_U = (List) divde_N1.get("U");
                List divde_D = (List) divde_N1.get("D");

                if (!divde_I.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    int rtnI = cosService.insertQry("cos1020_insert", param);
                    if(rtnI > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                    divde = divde_I;
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = cosService.updateQry("cos1020_update", param);
                    if(rtnU > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                    divde = divde_U;
                }

                if (!divde_D.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_D);
                    param.put("userId", userInfo.getName());
                    int rtnD = cosService.deleteQry("cos1020_delete", param);
                    if(rtnD > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                    divde = divde_D;
                }
            }
            if(rtn == "0") {
                rtnMsg = "정상 처리되었습니다.";
                secondaryTransactionManager.commit(status);
            } else {
                rtnMsg = "비정상 처리되었습니다.";
                secondaryTransactionManager.rollback(status);
            }
        } catch (DataIntegrityViolationException e) {
            secondaryTransactionManager.rollback(status);
            if (e.getCause() instanceof org.springframework.dao.DuplicateKeyException) {
                rtn = "3";
                rtnMsg = "중복된 자료가 존재합니다. 자료를 검토하고 다시 입력하세요.";
            } else {
                rtn = "3";
                if (e.getCause() instanceof SQLException sqlException) {
                    rtnMsg = "처리실패 : " + sqlException.getMessage();  // Get the specific error message from SQLException
                } else {
                    rtnMsg = "예상치 못한 오류가 발생했습니다.";
                }
            }
        }
        map.put("rtn", rtn);
        map.put("rtnMsg", rtnMsg);
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }


    /* *******************************************************************************
     ** 도서 제작비용 조정
     ** ******************************************************************************* */
    @PostMapping("/cos1030_list")
    public String cos1030_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
        Object result;

        String jsonDataRtn = "";
        RequestUtil requestUtil = new RequestUtil();
        JsonUtils jsonUtils = new JsonUtils();

        String jsonData = requestUtil.getBody(request);

        Map<String, Object> reqParam = new HashMap<String, Object>();
        if (!jsonData.isEmpty()) {
            reqParam = jsonUtils.jsonStringToMap(jsonData);
        }
        try {
            result = cosService.selectQryList("cos1030_list", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
//            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }

    @PostMapping("/cos1030_selected_list")
    public String cos1030_selected_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
        Object result;

        String jsonDataRtn = "";
        RequestUtil requestUtil = new RequestUtil();
        JsonUtils jsonUtils = new JsonUtils();

        String jsonData = requestUtil.getBody(request);

        Map<String, Object> reqParam = new HashMap<String, Object>();
        if (!jsonData.isEmpty()) {
            reqParam = jsonUtils.jsonStringToMap(jsonData);
        }
        try {
            result = cosService.selectQryList("cos1030_selected_list", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
//            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }


    /* *******************************************************************************
     ** 손익계산작업
     ** ******************************************************************************* */
    @PostMapping("/cos3010_procedure")
    public String cos3010_procedure(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);
        String userId = userInfo.getName();

        RequestUtil reqUtil = new RequestUtil();
        String jsonData = reqUtil.getBody(request);
        JsonUtils jsonUtil = new JsonUtils();
        Map<String, Object> reqParam = jsonUtil.jsonStringToMap(jsonData);
        String pYear = (String) reqParam.get("paramYear");
        String pMonth = (String) reqParam.get("paramMonth");

        String jsonDataRtn = "";
        String rtn = "0";
        String rtnMsg = "";
        Map<String, Object> map = new HashMap();

        boolean result = false;
        try {
            Connection con = null;
            CallableStatement cs = null;
            // 연결
//            con = DriverManager.getConnection(url, id, pw);
            con = DriverManager.getConnection(DB_URL, DB_USERID, DB_PASSWORD);

            // 프로시저 호출
            cs = con.prepareCall("{call PROC_COS_COMP(?, ?, ?, ?)}");

            // 입력 파라미터 설정
            cs.setString(1, pYear);
            cs.setString(2, pMonth);
            cs.setString(3, userId);
            // 출력 파라미터 설정
            cs.registerOutParameter(4, Types.BOOLEAN);

            // 실행
            cs.execute();
            // 출력 파라미터 값 가져오기
            result = cs.getBoolean(4);

            System.out.println("Procedure executed successfully :: " + result);
            rtn = "0";
            rtnMsg = "정상적으로 처리되었습니다";
            cs.close();
            con.close();
        } catch (Exception e) {
            rtn = "3";
            if (e.getCause() instanceof SQLException sqlException) {
                rtnMsg = "처리실패 : " + sqlException.getMessage();  // Get the specific error message from SQLException
            } else {
                rtnMsg = "예상치 못한 오류가 발생했습니다.";
            }
        }
        if(result) {
            rtn = "0";
        } else {
            rtn = "1";
        }
        map.put("rtn", rtn);
        map.put("rtnMsg", rtnMsg);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }

    /* *******************************************************************************
     ** 손익계산 취소작업
     ** ******************************************************************************* */
    @PostMapping("/cos3020_procedure")
    public String cos3020_procedure(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);
        String userId = userInfo.getName();

        RequestUtil reqUtil = new RequestUtil();
        String jsonData = reqUtil.getBody(request);
        JsonUtils jsonUtil = new JsonUtils();
        Map<String, Object> reqParam = jsonUtil.jsonStringToMap(jsonData);
        String pYear = (String) reqParam.get("paramYear");
        String pMonth = (String) reqParam.get("paramMonth");

        String jsonDataRtn = "";
        String rtn = "0";
        String rtnMsg = "";
        Map<String, Object> map = new HashMap();

        boolean result = false;
        try {
            Connection con = null;
            CallableStatement cs = null;
            // 연결
//            con = DriverManager.getConnection(url, id, pw);
            con = DriverManager.getConnection(DB_URL, DB_USERID, DB_PASSWORD);

            // 프로시저 호출
            cs = con.prepareCall("{call PROC_COS_CLEAR(?, ?, ?, ?)}");

            // 입력 파라미터 설정
            cs.setString(1, pYear);
            cs.setString(2, pMonth);
            cs.setString(3, userId);
            // 출력 파라미터 설정
            cs.registerOutParameter(4, Types.BOOLEAN);

            // 실행
            cs.execute();
            // 출력 파라미터 값 가져오기
            result = cs.getBoolean(4);

            System.out.println("Procedure executed successfully :: " + result);
            rtn = "0";
            rtnMsg = "정상적으로 처리되었습니다";
            cs.close();
            con.close();
        } catch (Exception e) {
            rtn = "3";
            if (e.getCause() instanceof SQLException sqlException) {
                rtnMsg = "처리실패 : " + sqlException.getMessage();  // Get the specific error message from SQLException
            } else {
                rtnMsg = "예상치 못한 오류가 발생했습니다.";
            }
        }
        if(result) {
            rtn = "0";
        } else {
            rtn = "1";
        }
        map.put("rtn", rtn);
        map.put("rtnMsg", rtnMsg);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }


    /* *******************************************************************************
     ** 도서 손익 분기표
     ** ******************************************************************************* */
    @PostMapping("/cos4010_list")
    public String cos4010_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
        Object result;

        String jsonDataRtn = "";
        RequestUtil requestUtil = new RequestUtil();
        JsonUtils jsonUtils = new JsonUtils();

        String jsonData = requestUtil.getBody(request);

        Map<String, Object> reqParam = new HashMap<String, Object>();
        if (!jsonData.isEmpty()) {
            reqParam = jsonUtils.jsonStringToMap(jsonData);
        }
        try {
            result = cosService.selectQryList("cos4010_list", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
//            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }


}
