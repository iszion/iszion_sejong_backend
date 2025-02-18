package com.iszion.api.com.controller;

import com.iszion.api.com.service.ComService;
import com.iszion.api.comn.DataRequestUtil;
import com.iszion.api.comn.JsonUtils;
import com.iszion.api.comn.RequestUtil;
import com.iszion.api.config.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/com")
public class ComController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComController.class);
    private final ComService comService;
    private final JwtTokenProvider jwtTokenProvider;
//    private final PlatformTransactionManager primaryTransactionManager;  // 트랜잭션 매니저

    @Qualifier("primaryTransactionManager")  // Use @Qualifier to inject the specific transaction manager for primary
    private final PlatformTransactionManager primaryTransactionManager;
    private final PasswordEncoder passwordEncoder;

    @Value("${file.upload.folder}")
    private String UPLOAD_DIR;

    @Value("${spring.primary.datasource.url}")
    private String DB_URL;
    @Value("${spring.primary.datasource.username}")
    private String DB_USERID;
    @Value("${spring.primary.datasource.password}")
    private String DB_PASSWORD;



    /* *******************************************************************************
     ** 공통코드 List 부분
     ** ******************************************************************************* */
    @PostMapping("/xComm_option_list")
    public String xComm_option_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = comService.selectQryList("xComm_option_list", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }

    /* *******************************************************************************
     ** 고객사 회사정보 List 부분
     ** ******************************************************************************* */
    @PostMapping("/helpComp_list")
    public String helpComp_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = comService.selectQryList("helpComp_list", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }

    /* *******************************************************************************
     ** 메뉴 선택 List 부분
     ** ******************************************************************************* */
    @PostMapping("/menu_main_list")
    public String menu_main_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = comService.selectQryList("menu_main_list", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }

    /* *******************************************************************************
     ** 사용자정보 가져오기 부분
     ** ******************************************************************************* */
    @PostMapping("/user_info")
    public String user_info(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = comService.selectQryOne("user_info", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }
    

    /* *******************************************************************************
     ** 패스워드 확인 부분
     ** ******************************************************************************* */
    @PostMapping("/passwordCheck")
    public String passwordCheck(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
        //Object result;
        HashMap<String, Object> result;

        String jsonDataRtn = "";
        RequestUtil requestUtil = new RequestUtil();
        JsonUtils jsonUtils = new JsonUtils();
        String jsonData = requestUtil.getBody(request);
        Map<String, Object> reqParam = new HashMap<String, Object>();
        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!jsonData.isEmpty()) {
            reqParam = jsonUtils.jsonStringToMap(jsonData);
        }
        try {
            String password = (String) reqParam.get("paramOldPassword");
            // 비밀번호 암호화

            String encodedPassword = passwordEncoder.encode(password);
            reqParam.put("passwd", encodedPassword);

            result = comService.selectQryOne1("passwordCheck", reqParam);
            String getPassword = (String) result.get("PASSWD");
            System.out.println(passwordEncoder.matches(password, getPassword));

            Map<String, Object> jsonList = new HashMap<>();
            if (passwordEncoder.matches(password, getPassword)) {
                jsonList.put("data", true);
            } else {
                jsonList.put("data", false);
            }

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();
        }
        return jsonDataRtn;
    }

    @PostMapping("/passwdUpdate_save")
    public String passwdUpdate_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = primaryTransactionManager.getTransaction(def);
        // 트랜잭션 정의 끝


        String jsonDataRtn = "";
        String rtn = "0";
        String rtnMsg = "";
        List<?> divde = null;
        Map<String, Object> map = new HashMap();
        JsonUtils jsonUtil = new JsonUtils();
        DataRequestUtil reqUtil = new DataRequestUtil();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String jsonData = reqUtil.getBody(request);
        try {
            Map<String, Object> mapDivde = jsonUtil.jsonStringToMap(jsonData);
            Map divde_N1 = (Map) mapDivde.get("no1");
            if (divde_N1 != null) {
                List<Map<String, Object>> divde_U = (List) divde_N1.get("U");

                // 패스워드 암호화
                if (!divde_U.isEmpty()) {
                    Map<String, Object> result = divde_U.get(0);
                    String passwd = passwordEncoder.encode(result.get("newPasswd").toString());
                    result.put("passwd", passwd);
                    System.identityHashCode(divde_U);
                    System.identityHashCode(result);
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = comService.updateQry("passwdUpdate_save", param);
                    if (rtnU > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    divde = divde_U;
                }

            }
            if (rtn == "0") {
                rtnMsg = "정상 처리되었습니다.";
                primaryTransactionManager.commit(status);
            } else {
                rtnMsg = "비정상 처리되었습니다.";
                primaryTransactionManager.rollback(status);
            }
        } catch (Exception e) {
            primaryTransactionManager.rollback(status);
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
     ** 메뉴 group list 부분
     ** ******************************************************************************* */
    @PostMapping("/prog_group_list")
    public String prog_group_list(HttpServletRequest request) throws IOException {
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
            result = comService.selectQryList("prog_group_list", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }
    
    @PostMapping("/prog_group_list_comp")
    public String prog_group_list_comp(HttpServletRequest request) throws IOException {
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
            result = comService.selectQryList("prog_group_list_comp", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }


    /* *******************************************************************************
     ** 회사정보 처리부분 부분
     ** ******************************************************************************* */
    @PostMapping("/com1010_list")
    public String com1010_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = comService.selectQryList("com1010_list", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }

    @PostMapping("/com1010_select")
    public String com1010_select(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = comService.selectQryOne("com1010_select", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }

    @PostMapping("/com1010_key_generation")
    public String com1010_key_generation(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = comService.selectQryOne("com1010_key_generation", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }

    @PostMapping("/com1010_save")
    public String com1010_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = primaryTransactionManager.getTransaction(def);
        // 트랜잭션 정의 끝


        String jsonDataRtn = "";
        String rtn = "0";
        String rtnMsg = "";
        List<?> divde = null;
        Map<String, Object> map = new HashMap();
        JsonUtils jsonUtil = new JsonUtils();
        DataRequestUtil reqUtil = new DataRequestUtil();

        String jsonData = reqUtil.getBody(request);
//        String createKeyValue = "";

        try {
            Map<String, Object> mapDivde = jsonUtil.jsonStringToMap(jsonData);
            Map divde_N1 = (Map) mapDivde.get("no1");
            if (divde_N1 != null) {
                List divde_I = (List) divde_N1.get("I");
                List divde_U = (List) divde_N1.get("U");
                List divde_D = (List) divde_N1.get("D");
                int divCnt = divde_I.size() + divde_U.size() + divde_D.size();
                if (!divde_I.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    int rtnI = comService.insertQry("com1010_insert", param);
//                    createKeyValue = (String) param.get("makeCompCd"); // insert에서 생성된 키값 불러오기
                    if (rtnI > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    divde = divde_I;
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = comService.updateQry("com1010_update", param);
                    if (rtnU > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    divde = divde_U;
                }

                if (!divde_D.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_D);
                    param.put("userId", userInfo.getName());
                    int rtnD = comService.deleteQry("com1010_delete", param);
                    if (rtnD > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    divde = divde_D;
                }
            }
            if (rtn == "0") {
                rtnMsg = "정상 처리되었습니다.";
                primaryTransactionManager.commit(status);
            } else {
                rtnMsg = "비정상 처리되었습니다.";
                primaryTransactionManager.rollback(status);
            }
        } catch (Exception e) {
            primaryTransactionManager.rollback(status);
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
//        map.put("createKeyValue", createKeyValue);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }


    @PostMapping("/com1010_select_user")
    public String com1010_select_user(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = comService.selectQryOne("com1010_select_user", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }


    @PostMapping("/com1010_save_user")
    public String com1010_save_user(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = primaryTransactionManager.getTransaction(def);
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
            Map divde_N1 = (Map) mapDivde.get("no1");
            if (divde_N1 != null) {
                List divde_U = (List) divde_N1.get("U");


                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = comService.updateQry("com1010_update_user", param);
                    if (rtnU > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    divde = divde_U;
                }

            }
            if (rtn == "0") {
                rtnMsg = "정상 처리되었습니다.";
                primaryTransactionManager.commit(status);
            } else {
                rtnMsg = "비정상 처리되었습니다.";
                primaryTransactionManager.rollback(status);
            }
        } catch (Exception e) {
            primaryTransactionManager.rollback(status);
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
     ** 사원정보 처리부분 부분
     ** ******************************************************************************* */
    @PostMapping("/com1020_list")
    public String com1020_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = comService.selectQryList("com1020_list", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }

    @PostMapping("/com1020_select")
    public String com1020_select(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = comService.selectQryOne("com1020_select", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }

    @PostMapping("/com1020_userId_check")
    public String com1020_userId_check(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = comService.selectQryOne("com1020_userId_check", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }

    @PostMapping("/com1020_save")
    public String com1020_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = primaryTransactionManager.getTransaction(def);
        // 트랜잭션 정의 끝


        String jsonDataRtn = "";
        String rtn = "0";
        String rtnMsg = "";
        List<?> divde = null;
        Map<String, Object> map = new HashMap();
        JsonUtils jsonUtil = new JsonUtils();
        DataRequestUtil reqUtil = new DataRequestUtil();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String jsonData = reqUtil.getBody(request);
        try {
            Map<String, Object> mapDivde = jsonUtil.jsonStringToMap(jsonData);
            Map divde_N1 = (Map) mapDivde.get("no1");
            if (divde_N1 != null) {
                List<Map<String, Object>> divde_I = (List) divde_N1.get("I");
                List divde_U = (List) divde_N1.get("U");
                List divde_D = (List) divde_N1.get("D");
                int divCnt = divde_I.size() + divde_U.size() + divde_D.size();

                // 패스워드 암호화
                if (!divde_I.isEmpty()) {
                    Map<String, Object> result = divde_I.get(0);
                    String passwd = passwordEncoder.encode(result.get("userId").toString());
                    result.put("passwd", passwd);
                    System.identityHashCode(divde_I);
                    System.identityHashCode(result);
                }

                if (!divde_I.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    int rtnI = comService.insertQry("com1020_insert", param);
                    //int rtnI = 0;
                    if (rtnI > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    divde = divde_I;
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = comService.updateQry("com1020_update", param);
                    if (rtnU > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    divde = divde_U;
                }

                if (!divde_D.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_D);
                    param.put("userId", userInfo.getName());
                    int rtnD = comService.deleteQry("com1020_delete", param);
                    if (rtnD > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    divde = divde_D;
                }
            }
            if (rtn == "0") {
                rtnMsg = "정상 처리되었습니다.";
                primaryTransactionManager.commit(status);
            } else {
                rtnMsg = "비정상 처리되었습니다.";
                primaryTransactionManager.rollback(status);
            }
        } catch (Exception e) {
            primaryTransactionManager.rollback(status);
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
     ** 사원정보 처리부분 부분
     ** ******************************************************************************* */
    @PostMapping("/com1020_list_user")
    public String com1020_list_user(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = comService.selectQryList("com1020_list_user", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }

    @PostMapping("/com1020_select_user")
    public String com1020_select_user(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = comService.selectQryOne("com1020_select_user", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }

    @PostMapping("/com1020_save_user")
    public String com1020_save_user(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = primaryTransactionManager.getTransaction(def);
        // 트랜잭션 정의 끝


        String jsonDataRtn = "";
        String rtn = "0";
        String rtnMsg = "";
        List<?> divde = null;
        Map<String, Object> map = new HashMap();
        JsonUtils jsonUtil = new JsonUtils();
        DataRequestUtil reqUtil = new DataRequestUtil();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String jsonData = reqUtil.getBody(request);
        try {
            Map<String, Object> mapDivde = jsonUtil.jsonStringToMap(jsonData);
            Map divde_N1 = (Map) mapDivde.get("no1");
            if (divde_N1 != null) {
                List divde_U = (List) divde_N1.get("U");
                List divde_D = (List) divde_N1.get("D");

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = comService.updateQry("com1020_update_user", param);
                    if (rtnU > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    divde = divde_U;
                }

            }
            if (rtn == "0") {
                rtnMsg = "정상 처리되었습니다.";
                primaryTransactionManager.commit(status);
            } else {
                rtnMsg = "비정상 처리되었습니다.";
                primaryTransactionManager.rollback(status);
            }
        } catch (Exception e) {
            primaryTransactionManager.rollback(status);
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


    @PostMapping(path = "/com1020_fileSave")
    public ResponseEntity<?> com1020_fileSave(@RequestParam("file") MultipartFile file, @RequestParam("empCd")String empCd) throws IOException {

        try {
            String originalFileName = file.getOriginalFilename();
            File folder = new File(UPLOAD_DIR);
            File destination = new File(UPLOAD_DIR + "/images/" + empCd+"_"+originalFileName);

            if (!folder.exists()) {
                folder.mkdir();
            }
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("empCd", empCd);
            param.put("IMAGE_FILE_NM", empCd+"_"+originalFileName);
            param.put("IMAGE_FILE_NM_FULL", UPLOAD_DIR + "/images/" + empCd+"_"+originalFileName);

            file.transferTo(destination);

            // 썸네일 파일 경로 설정
            String thumbnailFileName = empCd +"_"+ originalFileName;
            File thumbnailDestination = new File(UPLOAD_DIR + "/images/thumb/" + thumbnailFileName);

            // 썸네일 생성 및 저장 (썸네일 크기는 100x100으로 설정)
            Thumbnails.of(destination)
                .size(200, 200)
                .toFile(thumbnailDestination);

            // 썸네일 파일 경로도 파라미터에 추가
//            param.put("THUMBNAIL_FILE_NM", thumbnailFileName);
//            param.put("THUMBNAIL_FILE_NM_FULL", thumbnailDestination.getAbsolutePath());

            // 데이터베이스 업데이트 (썸네일 경로 포함)
            int result = comService.updateQry("com1020_fileSave", param);


            return ResponseEntity.ok("SUCCESS");
        } catch (Exception e) {
            return ResponseEntity.ok("ERROR : " + e.getMessage());
        }
    }

    @DeleteMapping("/com1020_fileDelete")
    public ResponseEntity<?> com1020_fileDelete(@RequestParam String filename,
                                                @RequestParam String empCd) {

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = primaryTransactionManager.getTransaction(def);
        // 트랜잭션 정의 끝

        File file = new File(UPLOAD_DIR + "/images/" + filename);
        File fileThumb = new File(UPLOAD_DIR + "/images/thumb/" + filename);

        try {
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("empCd", empCd);
            param.put("IMAGE_FILE_NM", filename);
            int result = comService.deleteQry("com1020_fileDelete", param);

            if (file.exists() && file.delete() && fileThumb.exists() && fileThumb.delete() && result > 0) {
                primaryTransactionManager.commit(status);
                return ResponseEntity.ok("SUCCESS");
            }
            primaryTransactionManager.rollback(status);
            return ResponseEntity.ok("ERROR");
        } catch (Exception e) {
            primaryTransactionManager.rollback(status);
            return ResponseEntity.ok("ERROR : " + e.getMessage());
        }
    }


    @PostMapping("/com1020_save_passwordReset")
    public String com1020_save_passwordReset(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = primaryTransactionManager.getTransaction(def);
        // 트랜잭션 정의 끝


        String jsonDataRtn = "";
        String rtn = "0";
        String rtnMsg = "";
        List<?> divde = null;
        Map<String, Object> map = new HashMap();
        JsonUtils jsonUtil = new JsonUtils();
        DataRequestUtil reqUtil = new DataRequestUtil();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String jsonData = reqUtil.getBody(request);
        try {
            Map<String, Object> mapDivde = jsonUtil.jsonStringToMap(jsonData);
            Map divde_N1 = (Map) mapDivde.get("no1");
            if (divde_N1 != null) {
                List<Map<String, Object>> divde_U = (List) divde_N1.get("U");

                // 패스워드 암호화
                if (!divde_U.isEmpty()) {
                    Map<String, Object> result = divde_U.get(0);
                    System.out.println("======>>> "+ result.get("userId").toString());
                    String passwd = passwordEncoder.encode(result.get("userId").toString());
                    result.put("passwd", passwd);
                    System.identityHashCode(divde_U);
                    System.identityHashCode(result);
                }


                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = comService.updateQry("com1020_update_passwordReset", param);
                    if (rtnU > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    divde = divde_U;
                }
            }

            if (rtn == "0") {
                rtnMsg = "정상 처리되었습니다.";
                primaryTransactionManager.commit(status);
            } else {
                rtnMsg = "비정상 처리되었습니다.";
                primaryTransactionManager.rollback(status);
            }
        } catch (Exception e) {
            primaryTransactionManager.rollback(status);
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
     ** 사용자 권한주기 처리부분 부분
     ** ******************************************************************************* */
    @PostMapping("/com2010_grntg_list")
    public String com2010_grntg_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
        System.out.println("com2010_grntg_list.........");
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
            result = comService.selectQryList("com2010_grntg_list", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }

    @PostMapping("/com2010_grntg_save")
    public String com2010_grntg_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = primaryTransactionManager.getTransaction(def);
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
            Map divde_N1 = (Map) mapDivde.get("no1");
            if (divde_N1 != null) {
                List divde_I = (List) divde_N1.get("I");
                List divde_U = (List) divde_N1.get("U");
                List divde_D = (List) divde_N1.get("D");
                int divCnt = divde_I.size() + divde_U.size() + divde_D.size();

                if (!divde_I.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    int rtnI = comService.insertQry("com2010_grntg_insert", param);
                    if (rtnI > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    System.out.println("== II =>> " + rtnI + " === " + rtn);
                    divde = divde_I;
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = comService.updateQry("com2010_grntg_update", param);
                    if (rtnU > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    System.out.println("== UU =>> " + rtnU + " === " + rtn);
                    divde = divde_U;
                }

                if (!divde_D.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_D);
                    param.put("userId", userInfo.getName());
                    int rtnD = comService.deleteQry("com2010_grntg_delete", param);
                    if (rtnD > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    System.out.println("== D =>> " + rtnD + " === " + rtn);
                    divde = divde_D;
                }
            }
            if (rtn == "0") {
                rtnMsg = "정상 처리되었습니다.";
                primaryTransactionManager.commit(status);
            } else {
                rtnMsg = "비정상 처리되었습니다.";
                primaryTransactionManager.rollback(status);
            }
        } catch (Exception e) {
            primaryTransactionManager.rollback(status);
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
     ** 프로그램 권한주기 처리부분 부분
     ** ******************************************************************************* */
    @PostMapping("/com2010_grntp_list")
    public String com2010_grntp_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
        System.out.println("com2010_grntp_list.........");
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
            result = comService.selectQryList("com2010_grntp_list", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }

    @PostMapping("/com2010_grntp_save")
    public String com2010_grntp_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = primaryTransactionManager.getTransaction(def);
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
            Map divde_N1 = (Map) mapDivde.get("no1");
            if (divde_N1 != null) {
                List divde_I = (List) divde_N1.get("I");
                List divde_U = (List) divde_N1.get("U");
                List divde_D = (List) divde_N1.get("D");
                int divCnt = divde_I.size() + divde_U.size() + divde_D.size();
                if (!divde_I.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    int rtnI = comService.insertQry("com2010_grntp_insert", param);
                    if (rtnI > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    System.out.println("== I =>> " + rtnI + " === " + rtn);
                    divde = divde_I;
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = comService.updateQry("com2010_grntp_update", param);
                    if (rtnU > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    System.out.println("== U =>> " + rtnU + " === " + rtn);
                    divde = divde_U;
                }

                if (!divde_D.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_D);
                    param.put("userId", userInfo.getName());
                    int rtnD = comService.deleteQry("com2010_grntp_delete", param);
                    if (rtnD > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    System.out.println("== D =>> " + rtnD + " === " + rtn);
                    divde = divde_D;
                }
            }
            if (rtn == "0") {
                rtnMsg = "정상 처리되었습니다.";
                primaryTransactionManager.commit(status);
            } else {
                rtnMsg = "비정상 처리되었습니다.";
                primaryTransactionManager.rollback(status);
            }
        } catch (Exception e) {
            primaryTransactionManager.rollback(status);
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
     ** 사원정보 불러오기 (권한주기용) 부분
     ** ******************************************************************************* */
    @PostMapping("/com2020_list")
    public String com2020_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
        System.out.println("com2020_list.........");
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
            result = comService.selectQryList("com2020_list", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }

    @PostMapping("/com2020_save")
    public String com2020_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = primaryTransactionManager.getTransaction(def);
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
            Map divde_N1 = (Map) mapDivde.get("no1");
            if (divde_N1 != null) {
                List divde_I = (List) divde_N1.get("I");
                List divde_U = (List) divde_N1.get("U");
                List divde_D = (List) divde_N1.get("D");
                int divCnt = divde_I.size() + divde_U.size() + divde_D.size();

                if (!divde_D.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_D);
                    param.put("userId", userInfo.getName());
                    int rtnD = 0;
                    comService.deleteQry("com2020_delete_grntg", param);
                    comService.deleteQry("com2020_delete_grntp", param);
                }
                if (!divde_I.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    int rtnI = 0;
                    rtnI = comService.insertQry("com2020_insert_grntg", param);
                    if (rtnI > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    rtnI = comService.insertQry("com2020_insert_grntp", param);
                    if (rtnI > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    System.out.println("== II =>> " + rtnI + " === " + rtn);
                    divde = divde_I;
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = comService.updateQry("com2020_update_grntg", param);
                    if (rtnU > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    System.out.println("== UU =>> " + rtnU + " === " + rtn);
                    divde = divde_U;
                }

            }
            if (rtn == "0") {
                rtnMsg = "정상 처리되었습니다.";
                primaryTransactionManager.commit(status);
            } else {
                rtnMsg = "비정상 처리되었습니다.";
                primaryTransactionManager.rollback(status);
            }
        } catch (Exception e) {
            primaryTransactionManager.rollback(status);
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
     ** 사용자 권한주기 처리부분 부분
     ** ******************************************************************************* */
    @PostMapping("/com2030_user_list")
    public String com2030_user_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = comService.selectQryList("com2030_user_list", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }



    /* *******************************************************************************
     ** 메뉴얼관리(관리자용) 프로그램 list 부분
     ** ******************************************************************************* */

    @PostMapping("/com8010_list")
    public String com8010_list(HttpServletRequest request) throws IOException {
        Object result;
//        String groupCd = request.getParameter("groupCd");
        String jsonDataRtn = "";
        RequestUtil requestUtil = new RequestUtil();
        JsonUtils jsonUtils = new JsonUtils();

        String jsonData = requestUtil.getBody(request);
        Map<String, Object> reqParam = new HashMap<String, Object>();
        if (!jsonData.isEmpty()) {
            reqParam = jsonUtils.jsonStringToMap(jsonData);
            System.out.println("reqParam : " + reqParam);
        }
        try {
            result = comService.selectQryList("com8010_list", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }


    @PostMapping("/com8010_docA_select")
    public String com8010_docA_select(HttpServletRequest request) throws IOException {
        System.out.println("com8010_docA_select.........");
        Object result;
//        String groupCd = request.getParameter("groupCd");
        String jsonDataRtn = "";
        RequestUtil requestUtil = new RequestUtil();
        JsonUtils jsonUtils = new JsonUtils();

        String jsonData = requestUtil.getBody(request);
        Map<String, Object> reqParam = new HashMap<String, Object>();
        if (!jsonData.isEmpty()) {
            reqParam = jsonUtils.jsonStringToMap(jsonData);
            System.out.println("reqParam : " + reqParam);
        }
        try {
            result = comService.selectQryList("com8010_docA_select", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }


    @PostMapping("/com8010_docA_save")
    public String com8010_docA_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = primaryTransactionManager.getTransaction(def);
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
            Map divde_N1 = (Map) mapDivde.get("no1");
            if (divde_N1 != null) {
                List divde_I = (List) divde_N1.get("I");
                List divde_U = (List) divde_N1.get("U");
                List divde_D = (List) divde_N1.get("D");
                int divCnt = divde_I.size() + divde_U.size() + divde_D.size();
                if (!divde_I.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    int rtnI = comService.insertQry("com8010_docA_insert", param);
                    if (rtnI > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    System.out.println("== I =>> " + rtnI + " === " + rtn);
                    divde = divde_I;
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = comService.updateQry("com8010_docA_update", param);
                    if (rtnU > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    System.out.println("== U =>> " + rtnU + " === " + rtn);
                    divde = divde_U;
                }

                if (!divde_D.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_D);
                    param.put("userId", userInfo.getName());
                    int rtnD = comService.deleteQry("com8010_docA_delete", param);
                    if (rtnD > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    System.out.println("== D =>> " + rtnD + " === " + rtn);
                    divde = divde_D;
                }
            }
            if (rtn == "0") {
                rtnMsg = "정상 처리되었습니다.";
                primaryTransactionManager.commit(status);
            } else {
                rtnMsg = "비정상 처리되었습니다.";
                primaryTransactionManager.rollback(status);
            }
        } catch (Exception e) {
            primaryTransactionManager.rollback(status);
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
     ** 메뉴정보 그룹 list 부분
     ** ******************************************************************************* */
    @PostMapping("/com5010_group_list")
    public String com5010_group_list(HttpServletRequest request) throws IOException {
        System.out.println("com5010_group_list.........");
        Object result;
//        String groupCd = request.getParameter("groupCd");
        String jsonDataRtn = "";
        RequestUtil requestUtil = new RequestUtil();
        JsonUtils jsonUtils = new JsonUtils();

        String jsonData = requestUtil.getBody(request);
        Map<String, Object> reqParam = new HashMap<String, Object>();
        if (!jsonData.isEmpty()) {
            reqParam = jsonUtils.jsonStringToMap(jsonData);
            System.out.println("reqParam : " + reqParam);
        }
        try {
            result = comService.selectQryList("com5010_group_list", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }

    /* *******************************************************************************
     ** 메뉴정보 전체 list 부분
     ** ******************************************************************************* */
    @PostMapping("/com5010_menu_list")
    public String com5010_menu_list(HttpServletRequest request) throws IOException {
        System.out.println("com5010_menu_list.........");
        Object result;
//        String groupCd = request.getParameter("groupCd");
        String jsonDataRtn = "";
        RequestUtil requestUtil = new RequestUtil();
        JsonUtils jsonUtils = new JsonUtils();

        String jsonData = requestUtil.getBody(request);
        Map<String, Object> reqParam = new HashMap<String, Object>();
        if (!jsonData.isEmpty()) {
            reqParam = jsonUtils.jsonStringToMap(jsonData);
            System.out.println("reqParam : " + reqParam);
        }
        try {
            result = comService.selectQryList("com5010_menu_list", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }

    @PostMapping("/com5010_save")
    public String com5010_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = primaryTransactionManager.getTransaction(def);
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
            Map divde_N1 = (Map) mapDivde.get("no1");
            if (divde_N1 != null) {
                List divde_I = (List) divde_N1.get("I");
                List divde_U = (List) divde_N1.get("U");
                List divde_D = (List) divde_N1.get("D");
                int divCnt = divde_I.size() + divde_U.size() + divde_D.size();
                if (!divde_I.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    int rtnI = comService.insertQry("com5010_insert", param);
                    if (rtnI > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    System.out.println("== I =>> " + rtnI + " === " + rtn);
                    divde = divde_I;
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = comService.updateQry("com5010_update", param);
                    if (rtnU > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    System.out.println("== U =>> " + rtnU + " === " + rtn);
                    divde = divde_U;
                }

                if (!divde_D.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_D);
                    param.put("userId", userInfo.getName());
                    int rtnD = comService.deleteQry("com5010_delete", param);
                    if (rtnD > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    System.out.println("== D =>> " + rtnD + " === " + rtn);
                    divde = divde_D;
                }
            }
            if (rtn == "0") {
                rtnMsg = "정상 처리되었습니다.";
                primaryTransactionManager.commit(status);
            } else {
                rtnMsg = "비정상 처리되었습니다.";
                primaryTransactionManager.rollback(status);
            }
        } catch (Exception e) {
            primaryTransactionManager.rollback(status);
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
     ** 메뉴설정 프로그램 list 부분
     ** ******************************************************************************* */
//    @RequestMapping(value="/com5020_prog_list", produces="application/json; charset=utf8" , method = RequestMethod.POST)
    @PostMapping("/com5020_prog_list")
    public String com5020_prog_list(HttpServletRequest request) throws IOException {
        System.out.println("com5020_prog_list.........");
        Object result;
//        String groupCd = request.getParameter("groupCd");
        String jsonDataRtn = "";
        RequestUtil requestUtil = new RequestUtil();
        JsonUtils jsonUtils = new JsonUtils();

        String jsonData = requestUtil.getBody(request);
        Map<String, Object> reqParam = new HashMap<String, Object>();
        if (!jsonData.isEmpty()) {
            reqParam = jsonUtils.jsonStringToMap(jsonData);
            System.out.println("reqParam : " + reqParam);
        }
        try {
            result = comService.selectQryList("com5020_prog_list", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }

    /* *******************************************************************************
     ** 메뉴설정 선택된 메뉴 list 부분
     ** ******************************************************************************* */
//    @RequestMapping(value="/com5020_menu_list", produces="application/json; charset=utf8" , method = RequestMethod.POST)
    @PostMapping("/com5020_menu_list")
    public String com5020_menu_list(HttpServletRequest request) throws IOException {
        System.out.println("com5020_menu_list.........");
        Object result;
//        String groupCd = request.getParameter("groupCd");
        String jsonDataRtn = "";
        RequestUtil requestUtil = new RequestUtil();
        JsonUtils jsonUtils = new JsonUtils();

        String jsonData = requestUtil.getBody(request);
        Map<String, Object> reqParam = new HashMap<String, Object>();
        if (!jsonData.isEmpty()) {
            reqParam = jsonUtils.jsonStringToMap(jsonData);
            System.out.println("reqParam : " + reqParam);
        }
        try {
            result = comService.selectQryList("com5020_menu_list", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }

    @PostMapping("/com5020_save")
    public String com5020_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = primaryTransactionManager.getTransaction(def);
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
            Map divde_N1 = (Map) mapDivde.get("no1");
            if (divde_N1 != null) {
                List divde_I = (List) divde_N1.get("I");
                List divde_U = (List) divde_N1.get("U");
                List divde_D = (List) divde_N1.get("D");
                int divCnt = divde_I.size() + divde_U.size() + divde_D.size();

                if (!divde_D.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_D);
                    param.put("userId", userInfo.getName());
                    int rtnD = comService.deleteQry("com5020_delete", param);
                    if (rtnD > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    divde = divde_D;
                }

                if (!divde_I.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    int rtnI = comService.insertQry("com5020_insert", param);
                    if (rtnI > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    divde = divde_I;
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = comService.updateQry("com5020_update", param);
                    if (rtnU > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    divde = divde_U;
                }
            }
            if (rtn == "0") {
                rtnMsg = "정상 처리되었습니다.";
                primaryTransactionManager.commit(status);
            } else {
                rtnMsg = "비정상 처리되었습니다.";
                primaryTransactionManager.rollback(status);
            }
        } catch (Exception e) {
            primaryTransactionManager.rollback(status);
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

    @PostMapping("/com5020_select_delete")
    public String com5020_select_delete(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = primaryTransactionManager.getTransaction(def);
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
            Map divde_N1 = (Map) mapDivde.get("no1");
            if (divde_N1 != null) {
                List divde_I = (List) divde_N1.get("I");
                List divde_U = (List) divde_N1.get("U");
                List divde_D = (List) divde_N1.get("D");
                int divCnt = divde_I.size() + divde_U.size() + divde_D.size();

                if (!divde_D.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_D);
                    System.out.println("========>>> " + param);
                    int rtn1 = comService.deleteQry("com5020_select_delete", param);
                    System.out.println("=====>>>  " + rtn1);
                    divde = divde_D;
                }

            }
            primaryTransactionManager.commit(status);
        } catch (Exception e) {
            primaryTransactionManager.rollback(status);
            rtn = "1";
            e.printStackTrace();
        }
        map.put("rtn", rtn);
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");

        return jsonDataRtn;
    }
    
    
    

    /* *******************************************************************************
     ** 시스템관리자 공통코드 처리부분 부분
     ** ******************************************************************************* */
    @PostMapping("/com7010_list")
    public String com7010_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = comService.selectQryList("com7010_list", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }

    @PostMapping("/com7010_select")
    public String com7010_select(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = comService.selectQryList("com7010_select", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        return jsonDataRtn;
    }

    @PostMapping("/com7010_group_save")
    public String com7010_group_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {

        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = primaryTransactionManager.getTransaction(def);
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
            Map divde_N1 = (Map) mapDivde.get("no1");
            if (divde_N1 != null) {
                List divde_I = (List) divde_N1.get("I");
                List divde_U = (List) divde_N1.get("U");
                List divde_D = (List) divde_N1.get("D");
                int divCnt = divde_I.size() + divde_U.size() + divde_D.size();
                if (!divde_I.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    int rtnI = comService.insertQry("com7010_group_insert", param);
                    if(rtnI > 0)  { if(rtn =="0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                    divde = divde_I;
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = comService.updateQry("com7010_group_update", param);
                    rtnU = comService.updateQry("com7010_group_all_update", param);
                    if(rtnU > 0)  { if(rtn =="0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                    divde = divde_U;
                }

                if (!divde_D.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_D);
                    param.put("userId", userInfo.getName());
                    int rtnD = comService.deleteQry("com7010_group_delete", param);
                    if(rtnD > 0)  { if(rtn =="0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                    divde = divde_D;
                }
            }
            if(rtn == "0") {
                rtnMsg = "정상 처리되었습니다.";
                primaryTransactionManager.commit(status);
            } else {
                rtnMsg = "비정상 처리되었습니다.";
                primaryTransactionManager.rollback(status);
            }
        } catch (Exception e) {
            primaryTransactionManager.rollback(status);
            rtn = "3";
            if (e.getCause() instanceof SQLException sqlException) {
                rtnMsg = "처리실패 : " +  sqlException.getMessage();  // Get the specific error message from SQLException
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

    @PostMapping("/com7010_save")
    public String com7010_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {

        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);

        // 트랜잭션 정의
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = primaryTransactionManager.getTransaction(def);
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
            Map divde_N1 = (Map) mapDivde.get("no1");
            if (divde_N1 != null) {
                List divde_I = (List) divde_N1.get("I");
                List divde_U = (List) divde_N1.get("U");
                List divde_D = (List) divde_N1.get("D");
                int divCnt = divde_I.size() + divde_U.size() + divde_D.size();
                if (!divde_I.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    int rtnI = comService.insertQry("com7010_insert", param);
                    if(rtnI > 0)  { if(rtn =="0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                    divde = divde_I;
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = comService.updateQry("com7010_update", param);
                    if(rtnU > 0)  { if(rtn =="0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                    divde = divde_U;
                }

                if (!divde_D.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_D);
                    param.put("userId", userInfo.getName());
                    int rtnD = comService.deleteQry("com7010_delete", param);
                    if(rtnD > 0)  { if(rtn =="0") {rtn = "0";} else {rtn = "1"; }} else { rtn = "1"; }
                    divde = divde_D;
                }
            }
            if(rtn == "0") {
                rtnMsg = "정상 처리되었습니다.";
                primaryTransactionManager.commit(status);
            } else {
                rtnMsg = "비정상 처리되었습니다.";
                primaryTransactionManager.rollback(status);
            }
        } catch (Exception e) {
            primaryTransactionManager.rollback(status);
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

    
}
