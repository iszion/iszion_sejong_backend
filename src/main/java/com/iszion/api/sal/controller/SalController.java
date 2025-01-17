package com.iszion.api.sal.controller;

import com.iszion.api.comn.DataRequestUtil;
import com.iszion.api.comn.JsonUtils;
import com.iszion.api.comn.RequestUtil;
import com.iszion.api.config.jwt.JwtTokenProvider;
import com.iszion.api.sal.service.SalService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sal")
public class SalController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalController.class);
    private final SalService salService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PlatformTransactionManager transactionManager;  // 트랜잭션 매니저

    @Value("${file.upload.folder}")
    private String UPLOAD_DIR;

    /* *******************************************************************************
     ** 매입전표관리
     ** ******************************************************************************* */
    @PostMapping("/sal1010_list_event")
    public String sal1010_list_event(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal1010_list_event", reqParam);

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
    @PostMapping("/sal1010_list_search")
    public String sal1010_list_search(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal1010_list_search", reqParam);

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
    @PostMapping("/sal1010_list_header")
    public String sal1010_list_header(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal1010_list_header", reqParam);

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
    @PostMapping("/sal1010_select_header")
    public String sal1010_select_header(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal1010_select_header", reqParam);

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
    @PostMapping("/sal1010_select_list_details")
    public String sal1010_select_list_details(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal1010_select_list_details", reqParam);

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

    @PostMapping("/sal1010_maxSeqCheck")
    public String sal1010_maxSeqCheck(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal1010_maxSeqCheck", reqParam);

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


    @PostMapping("/sal1010_save")
    public String sal1010_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        // 트랜잭션 정의 끝


        String jsonDataRtn = "";
        String rtn = "0";
        String rtnMsg = "";
        List<?> divde = null;
        Map<String, Object> map = new HashMap();
        JsonUtils jsonUtil = new JsonUtils();
        DataRequestUtil reqUtil = new DataRequestUtil();

        String jsonData = reqUtil.getBody(request);

        String createKeyValue = "";

        try {
            Map<String, Object> mapDivde = jsonUtil.jsonStringToMap(jsonData);
            Map divde_N1 =  (Map) mapDivde.get("no1");
            Map divde_N2 =  (Map) mapDivde.get("no2");

            if (divde_N1 != null) {
                List divde_I = (List) divde_N1.get("I");
                List divde_U = (List) divde_N1.get("U");
                List divde_D = (List) divde_N1.get("D");

                if (!divde_I.isEmpty()) {

                    List<Map<String, Object>> divde_II = (List<Map<String, Object>>) divde_N1.get("I");
                    String stdDay = (String) divde_II.get(0).get("buyDay");

                    Map<String, Object> param = new HashMap<>();
                    param.put("stdDay", stdDay);
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    int rtnI = salService.insertQry("sal1010_insert_header", param);

                    createKeyValue = (String) param.get("seq"); // insert에서 생성된 키값 불러오기
                    // 아이템 자료에 불러온 키값 적용 업데이트
                    List<Map<String, Object>> divde_N2_I = (List<Map<String, Object>>) divde_N2.get("I");
                    if (divde_N2_I != null) {
                        for (Map<String, Object> item : divde_N2_I) {
                            item.put("seq", createKeyValue); // seq 값을 업데이트
                        }
                        divde_N2.put("I", divde_N2_I);   // 업데이트된 "I" 데이터를 다시 divde_N2에 반영
                    }

                    if(rtnI > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = salService.updateQry("sal1010_update_header", param);
                    if(rtnU > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                }

                if (!divde_D.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_D);
                    param.put("userId", userInfo.getName());
                    int rtnD = salService.deleteQry("sal1010_delete_header", param);
                    if(rtnD > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                        rtnD = salService.deleteQry("sal1010_delete_details_all", param);
                    if(rtnD >= 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                }
            }

            if (divde_N2 != null) {
                List divde_I = (List) divde_N2.get("I");
                List divde_U = (List) divde_N2.get("U");
                List divde_D = (List) divde_N2.get("D");

                if (!divde_I.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    int rtnI = salService.insertQry("sal1010_insert_details", param);
                    if(rtnI > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = salService.updateQry("sal1010_update_details", param);
                    if(rtnU > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                }

                if (!divde_D.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_D);
                    param.put("userId", userInfo.getName());
                    int rtnD = salService.deleteQry("sal1010_delete_details", param);
                    if(rtnD > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                }
            }


            if(rtn == "0") {
                rtnMsg = "정상 처리되었습니다.";
                transactionManager.commit(status);
            } else {
                rtnMsg = "비정상 처리되었습니다.";
                transactionManager.rollback(status);
            }
        } catch (Exception e) {
            transactionManager.rollback(status);
            rtn = "3";
            if (e.getCause() instanceof SQLException sqlException) {
                rtnMsg = "처리실패 : " + sqlException.getMessage();  // Get the specific error message from SQLException
            } else {
                rtnMsg = "예상치 못한 오류가 발생했습니다.";
            }
        }
        map.put("rtn", rtn);
        map.put("rtnMsg", rtnMsg);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }


    /* *******************************************************************************
     ** 매입전표현황
     ** ******************************************************************************* */
    @PostMapping("/sal1020_list")
    public String sal1020_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal1020_list", reqParam);

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
     ** 매출전표관리
     ** ******************************************************************************* */
    @PostMapping("/sal2010_list_event")
    public String sal2010_list_event(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2010_list_event", reqParam);

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
    @PostMapping("/sal2010_list_search")
    public String sal2010_list_search(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2010_list_search", reqParam);

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
    @PostMapping("/sal2010_list_header")
    public String sal2010_list_header(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2010_list_header", reqParam);

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
    @PostMapping("/sal2010_select_header")
    public String sal2010_select_header(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2010_select_header", reqParam);
            System.out.println("$$$$$$$$$######## sal2010_select_header");

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
    @PostMapping("/sal2010_select_list_details")
    public String sal2010_select_list_details(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2010_select_list_details", reqParam);

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

    @PostMapping("/sal2010_maxSeqCheck")
    public String sal2010_maxSeqCheck(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2010_maxSeqCheck", reqParam);

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

    @PostMapping("/sal2010_cust_seq_check")
    public String sal2010_cust_seq_check(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2010_cust_seq_check", reqParam);

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


    @PostMapping("/sal2010_save")
    public String sal2010_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        // 트랜잭션 정의 끝


        String jsonDataRtn = "";
        String rtn = "0";
        String rtnMsg = "";
        List<?> divde = null;
        Map<String, Object> map = new HashMap();
        JsonUtils jsonUtil = new JsonUtils();
        DataRequestUtil reqUtil = new DataRequestUtil();

        String jsonData = reqUtil.getBody(request);

        String createKeyValue = "";

        try {
            Map<String, Object> mapDivde = jsonUtil.jsonStringToMap(jsonData);
            Map divde_N1 =  (Map) mapDivde.get("no1");
            Map divde_N2 =  (Map) mapDivde.get("no2");

            if (divde_N1 != null) {
                List divde_I = (List) divde_N1.get("I");
                List divde_U = (List) divde_N1.get("U");
                List divde_D = (List) divde_N1.get("D");

                if (!divde_I.isEmpty()) {

                    List<Map<String, Object>> divde_II = (List<Map<String, Object>>) divde_N1.get("I");
                    String stdDay = (String) divde_II.get(0).get("dealDay");

                    Map<String, Object> param = new HashMap<>();
                    param.put("stdDay", stdDay);
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    int rtnI = salService.insertQry("sal2010_insert_header", param);

                    createKeyValue = (String) param.get("seq"); // insert에서 생성된 키값 불러오기
                    // 아이템 자료에 불러온 키값 적용 업데이트
                    List<Map<String, Object>> divde_N2_I = (List<Map<String, Object>>) divde_N2.get("I");
                    if (divde_N2_I != null) {
                        for (Map<String, Object> item : divde_N2_I) {
                            item.put("seq", createKeyValue); // seq 값을 업데이트
                        }
                        divde_N2.put("I", divde_N2_I);   // 업데이트된 "I" 데이터를 다시 divde_N2에 반영
                    }

                    if(rtnI > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = salService.updateQry("sal2010_update_header", param);
                    if(rtnU > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                }

                if (!divde_D.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_D);
                    param.put("userId", userInfo.getName());
                    int rtnD = salService.deleteQry("sal2010_delete_header", param);
                    if(rtnD > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                    rtnD = salService.deleteQry("sal2010_delete_details_all", param);
                    if(rtnD >= 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                }
            }

            if (divde_N2 != null) {
                List divde_I = (List) divde_N2.get("I");
                List divde_U = (List) divde_N2.get("U");
                List divde_D = (List) divde_N2.get("D");

                if (!divde_I.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    int rtnI = salService.insertQry("sal2010_insert_details", param);
                    if(rtnI > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = salService.updateQry("sal2010_update_details", param);
                    if(rtnU > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                }

                if (!divde_D.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_D);
                    param.put("userId", userInfo.getName());
                    int rtnD = salService.deleteQry("sal2010_delete_details", param);
                    if(rtnD > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                }
            }


            if(rtn == "0") {
                rtnMsg = "정상 처리되었습니다.";
                transactionManager.commit(status);
            } else {
                rtnMsg = "비정상 처리되었습니다.";
                transactionManager.rollback(status);
            }
        } catch (Exception e) {
            transactionManager.rollback(status);
            rtn = "3";
            if (e.getCause() instanceof SQLException sqlException) {
                rtnMsg = "처리실패 : " + sqlException.getMessage();  // Get the specific error message from SQLException
            } else {
                rtnMsg = "예상치 못한 오류가 발생했습니다.";
            }
        }
        map.put("rtn", rtn);
        map.put("rtnMsg", rtnMsg);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }


    /* *******************************************************************************
     ** 매출전표 코업접수 관리
     ** ******************************************************************************* */
    @PostMapping("/sal2020_list")
    public String sal2020_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2020_list", reqParam);

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

    @PostMapping("/sal2020_select_list")
    public String sal2020_select_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2020_select_list", reqParam);

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
     ** 반품전표 체크 관리
     ** ******************************************************************************* */
    @PostMapping("/sal2030_return_check_list")
    public String sal2030_return_check_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2030_return_check_list", reqParam);

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
     ** 출고전표현황
     ** ******************************************************************************* */
    @PostMapping("/sal2110_maxPages")
    public String sal2110_maxPages(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2110_maxPages", reqParam);

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

    @PostMapping("/sal2110_list")
    public String sal2110_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2110_list", reqParam);

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
     ** 출고전표 집계현황
     ** ******************************************************************************* */
    @PostMapping("/sal2120_list")
    public String sal2120_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2120_list", reqParam);

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
     ** 반품전표현황
     ** ******************************************************************************* */
    @PostMapping("/sal2130_maxPages")
    public String sal2130_maxPages(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2130_maxPages", reqParam);

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

    @PostMapping("/sal2130_list")
    public String sal2130_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2130_list", reqParam);

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
     ** 반품전표 집계현황
     ** ******************************************************************************* */
    @PostMapping("/sal2140_list")
    public String sal2140_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2140_list", reqParam);

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
     ** 도서별 판매현황
     ** ******************************************************************************* */
    @PostMapping("/sal2150_list")
    public String sal2150_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2150_list", reqParam);

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

   @PostMapping("/sal2150_select_list")
    public String sal2150_select_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2150_select_list", reqParam);

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
     ** 거래처별 판매현황
     ** ******************************************************************************* */
    @PostMapping("/sal2160_list")
    public String sal2160_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2160_list", reqParam);

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

   @PostMapping("/sal2160_select_list")
    public String sal2160_select_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2160_select_list", reqParam);

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
     ** 업체지점 판매현황
     ** ******************************************************************************* */
    @PostMapping("/sal2170_list")
    public String sal2170_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2170_list", reqParam);

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
     ** 매출액명세서(월)
     ** ******************************************************************************* */
    @PostMapping("/sal2180_list")
    public String sal2180_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2180_list", reqParam);

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
     ** 전자계산서자료 양식
     ** ******************************************************************************* */
    @PostMapping("/sal2190_list")
    public String sal2190_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal2190_list", reqParam);

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
     ** 입금전표관리
     ** ******************************************************************************* */
    @PostMapping("/sal3010_list")
    public String sal3010_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal3010_list", reqParam);

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

    @PostMapping("/sal3010_select_bill")
    public String sal3010_select_bill(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal3010_select_bill", reqParam);

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


    @PostMapping("/sal3010_save")
    public String sal3010_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
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
                    Map<String, Object> param = new HashMap<>();
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    int rtnI = salService.insertQry("sal3010_insert", param);
                    if(rtnI > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = salService.updateQry("sal3010_update", param);
                    if(rtnU > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                }

                if (!divde_D.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_D);
                    param.put("userId", userInfo.getName());
                    int rtnD = salService.deleteQry("sal3010_delete", param);
                    if(rtnD > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                    rtnD = salService.deleteQry("sal3010_delete_bill", param);
                    if(rtnD >= 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                }
            }


            if(rtn == "0") {
                rtnMsg = "정상 처리되었습니다.";
                transactionManager.commit(status);
            } else {
                rtnMsg = "비정상 처리되었습니다.";
                transactionManager.rollback(status);
            }
        } catch (Exception e) {
            transactionManager.rollback(status);
            rtn = "3";
            if (e.getCause() instanceof SQLException sqlException) {
                rtnMsg = "처리실패 : " + sqlException.getMessage();  // Get the specific error message from SQLException
            } else {
                rtnMsg = "예상치 못한 오류가 발생했습니다.";
            }
        }
        map.put("rtn", rtn);
        map.put("rtnMsg", rtnMsg);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }


    @PostMapping("/sal3010_save_bill")
    public String sal3010_save_bill(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
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
                    Map<String, Object> param = new HashMap<>();
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    int rtnI = salService.insertQry("sal3010_insert_bill", param);
                    if(rtnI > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = salService.updateQry("sal3010_update_bill", param);
                    if(rtnU > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                }

                if (!divde_D.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_D);
                    param.put("userId", userInfo.getName());
                    int rtnD = salService.deleteQry("sal3010_delete_bill", param);
                    if(rtnD > 0)  { if(rtn == "0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                }
            }


            if(rtn == "0") {
                rtnMsg = "정상 처리되었습니다.";
                transactionManager.commit(status);
            } else {
                rtnMsg = "비정상 처리되었습니다.";
                transactionManager.rollback(status);
            }
        } catch (Exception e) {
            transactionManager.rollback(status);
            rtn = "3";
            if (e.getCause() instanceof SQLException sqlException) {
                rtnMsg = "처리실패 : " + sqlException.getMessage();  // Get the specific error message from SQLException
            } else {
                rtnMsg = "예상치 못한 오류가 발생했습니다.";
            }
        }
        map.put("rtn", rtn);
        map.put("rtnMsg", rtnMsg);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }

    /* *******************************************************************************
     ** 어음전표관리
     ** ******************************************************************************* */
    @PostMapping("/sal3020_list")
    public String sal3020_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal3020_list", reqParam);

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
     ** 매출수급집계관리(월)
     ** ******************************************************************************* */
    @PostMapping("/sal3110_list")
    public String sal3110_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal3110_list", reqParam);

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
     ** 매출수급집계관리(년)
     ** ******************************************************************************* */
    @PostMapping("/sal3120_list")
    public String sal3120_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal3120_list", reqParam);

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
     ** 입금전표현황
     ** ******************************************************************************* */
    @PostMapping("/sal3210_list")
    public String sal3210_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal3210_list", reqParam);

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
     ** 어음전표현황
     ** ******************************************************************************* */
    @PostMapping("/sal3220_list")
    public String sal3220_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal3220_list", reqParam);

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
     ** 도서별재고현황
     ** ******************************************************************************* */
    @PostMapping("/sal4110_list")
    public String sal4110_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal4110_list", reqParam);

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
     ** 도서별적정재고현황
     ** ******************************************************************************* */
    @PostMapping("/sal4120_list")
    public String sal4120_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal4120_list", reqParam);

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
     ** 입출재고조회(월)
     ** ******************************************************************************* */
    @PostMapping("/sal4210_list")
    public String sal4210_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal4210_list", reqParam);

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
     ** 입출재고조회(년)
     ** ******************************************************************************* */
    @PostMapping("/sal4220_list")
    public String sal4220_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal4220_list", reqParam);

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
     ** 거래처 거래원장
     ** ******************************************************************************* */
    @PostMapping("/sal4310_list")
    public String sal4310_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal4310_list", reqParam);

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
     ** 도서별 입출고 원장
     ** ******************************************************************************* */
    @PostMapping("/sal4320_list")
    public String sal4320_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = salService.selectQryList("sal4320_list", reqParam);

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
