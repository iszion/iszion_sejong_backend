package com.iszion.api.sys.controller;

import com.iszion.api.comn.DataRequestUtil;
import com.iszion.api.comn.JsonUtils;
import com.iszion.api.comn.RequestUtil;
import com.iszion.api.config.jwt.JwtTokenProvider;
import com.iszion.api.sys.service.SysService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sys")
public class SysController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysController.class);
    private final SysService sysService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PlatformTransactionManager transactionManager;  // 트랜잭션 매니저

    private final PasswordEncoder passwordEncoder;


    /* *******************************************************************************
     ** 즐겨찾기 저장 부분
     ** ******************************************************************************* */
    @PostMapping("/fav_save")
    public String fav_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
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
                    int rtnI = sysService.insertQry("fav_insert", param);
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
                    int rtnU = sysService.updateQry("fav_update", param);
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
                    int rtnD = sysService.deleteQry("fav_delete", param);
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
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }

    /* *******************************************************************************
     ** 게시판 처리 부분
     ** ******************************************************************************* */
    @PostMapping("/noticeBoard_list")
    public String noticeBoard_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
        System.out.println("noticeBoard_list.........");
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
            result = sysService.selectQryList("noticeBoard_list", reqParam);

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

    @PostMapping("/noticeBoard_select")
    public String noticeBoard_select(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
        System.out.println("noticeBoard_select.........");
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
            result = sysService.selectQryList("noticeBoard_select", reqParam);

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


    @PostMapping("/noticeBoard_save")
    public String noticeBoard_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
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
                    int rtnI = sysService.insertQry("noticeBoard_insert", param);
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
                    int rtnU = sysService.updateQry("noticeBoard_update", param);
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
                    int rtnD = sysService.deleteQry("noticeBoard_delete", param);
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
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }

    /* *******************************************************************************
     ** 이벤트 처리 부분
     ** ******************************************************************************* */
    @PostMapping("/event_list")
    public String event_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
        System.out.println("event_list.........");
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
            result = sysService.selectQryList("event_list", reqParam);

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

    @PostMapping("/event_save")
    public String event_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
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
                    int rtnI = sysService.insertQry("event_insert", param);
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
                    int rtnU = sysService.updateQry("event_update", param);
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
                    int rtnD = sysService.deleteQry("event_delete", param);
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
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }

    /* *******************************************************************************
     ** 언어정보 가져오는 부분
     ** ******************************************************************************* */
    @PostMapping("/lang_list")
    public String lang_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
        System.out.println("lang_list.........");
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
            result = sysService.selectQryList("lang_list", reqParam);

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
        System.out.println("menu_main_list.........");
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
            result = sysService.selectQryList("menu_main_list", reqParam);

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
            result = sysService.selectQryList("user_info", reqParam);

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


    //    @RequestMapping(value="/menu_sub_list", produces="application/json; charset=utf8" , method = RequestMethod.POST)
    @PostMapping("/menu_sub_list")
    public String menu_sub_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        System.out.println("menu_sub_list.........");
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
            result = sysService.selectQryList("menu_sub_list", reqParam);

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

    // 즐겨찾기
    @PostMapping("/menu_fav_list")
    public String menu_fav_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        System.out.println("menu_fav_list.........");
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
            result = sysService.selectQryList("menu_fav_list", reqParam);

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
     ** 메뉴 group list 부분
     ** ******************************************************************************* */
//    @RequestMapping(value="/prog_group_list", produces="application/json; charset=utf8" , method = RequestMethod.GET)
    @PostMapping("/prog_group_list")
    public String prog_group_list(HttpServletRequest request) throws IOException {
        System.out.println("prog_group_list.........");
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
            result = sysService.selectQryList("prog_group_list", reqParam);

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
     ** 사원정보 처리부분 부분
     ** ******************************************************************************* */
    @PostMapping("/sys1010_list")
    public String sys1010_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
        System.out.println("sys1010_list.........");
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
            result = sysService.selectQryList("sys1010_list", reqParam);

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

    @PostMapping("/sys1010_select")
    public String sys1010_select(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
        System.out.println("sys1010_select.........");
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
            System.out.println("param ==> " + reqParam);
            result = sysService.selectQryList("sys1010_select", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        System.out.println("===>> " + jsonDataRtn);
        return jsonDataRtn;
    }

    @PostMapping("/sys1010_save")
    public String sys1010_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
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
                    int rtnI = sysService.insertQry("sys1010_insert", param);
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
                    System.out.println("== I =>> " + rtnI + " === " + rtn);
                    divde = divde_I;
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = sysService.updateQry("sys1010_update", param);
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
                    int rtnD = sysService.deleteQry("sys1010_delete", param);
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
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }

    @PostMapping("/sys1010_save_passwordReset")
    public String sys1010_save_passwordReset(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
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
                    String passwd = passwordEncoder.encode(result.get("userId").toString());
                    result.put("passwd", passwd);
                    System.identityHashCode(divde_U);
                    System.identityHashCode(result);
                }


                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    int rtnU = sysService.updateQry("sys1010_update_passwordReset", param);
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
            }

            if (rtn == "0") {
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
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }

    /* *******************************************************************************
     ** 사용자 권한주기 처리부분 부분
     ** ******************************************************************************* */
    @PostMapping("/sys1110_grntg_list")
    public String sys1110_grntg_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
        System.out.println("sys1110_grntg_list.........");
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
            result = sysService.selectQryList("sys1110_grntg_list", reqParam);

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

    @PostMapping("/sys1110_grntg_save")
    public String sys1110_grntg_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
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
                    int rtnI = sysService.insertQry("sys1110_grntg_insert", param);
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
                    int rtnU = sysService.updateQry("sys1110_grntg_update", param);
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
                    int rtnD = sysService.deleteQry("sys1110_grntg_delete", param);
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
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }

    /* *******************************************************************************
     ** 프로그램 권한주기 처리부분 부분
     ** ******************************************************************************* */
    @PostMapping("/sys1110_grntp_list")
    public String sys1110_grntp_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
        System.out.println("sys1110_grntp_list.........");
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
            result = sysService.selectQryList("sys1110_grntp_list", reqParam);

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

    @PostMapping("/sys1110_grntp_save")
    public String sys1110_grntp_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
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
                    int rtnI = sysService.insertQry("sys1110_grntp_insert", param);
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
                    int rtnU = sysService.updateQry("sys1110_grntp_update", param);
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
                    int rtnD = sysService.deleteQry("sys1110_grntp_delete", param);
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
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }

    /* *******************************************************************************
     ** 사원정보 불러오기 (권한주기용) 부분
     ** ******************************************************************************* */
    @PostMapping("/sys1120_list")
    public String sys1120_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
        System.out.println("sys1120_list.........");
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
            result = sysService.selectQryList("sys1120_list", reqParam);

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

    @PostMapping("/sys1120_save")
    public String sys1120_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
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
                    sysService.deleteQry("sys1120_delete_grntg", param);
                    sysService.deleteQry("sys1120_delete_grntp", param);
                }
                if (!divde_I.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    int rtnI = 0;
                    rtnI = sysService.insertQry("sys1120_insert_grntg", param);
                    if (rtnI > 0) {
                        if (rtn == "0") {
                            rtn = "0";
                        } else {
                            rtn = "1";
                        }
                    } else {
                        rtn = "1";
                    }
                    rtnI = sysService.insertQry("sys1120_insert_grntp", param);
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
                    int rtnU = sysService.updateQry("sys1120_update_grntg", param);
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
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }


    /* *******************************************************************************
     ** 사용자 권한주기 처리부분 부분
     ** ******************************************************************************* */
    @PostMapping("/sys1130_user_list")
    public String sys1130_user_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
        System.out.println("sys1130_user_list.........");
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
            result = sysService.selectQryList("sys1130_user_list", reqParam);

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
    @PostMapping("/sys1910_list")
    public String sys1910_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
        System.out.println("sys1910_list.........");
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
            result = sysService.selectQryList("sys1910_list", reqParam);

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

    @PostMapping("/sys1910_select")
    public String sys1910_select(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
        System.out.println("sys1910_select.........");
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
            System.out.println("param ==> " + reqParam);
            result = sysService.selectQryList("sys1910_select", reqParam);

            Map<String, Object> jsonList = new HashMap<>();
            jsonList.put("data", result);

            jsonDataRtn = jsonUtils.getToJson(jsonList);
            jsonDataRtn = jsonDataRtn.replaceAll("null", "\"\"");
            LOGGER.info("-------------------" + jsonDataRtn);

        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();

        }
        System.out.println("===>> " + jsonDataRtn);
        return jsonDataRtn;
    }

    @PostMapping("/sys1910_save")
    public String sys1910_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
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
                    int rtnI = sysService.insertQry("sys1910_insert", param);
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
                    int rtnU = sysService.updateQry("sys1910_update", param);
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
                    int rtnD = sysService.deleteQry("sys1910_delete", param);
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
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }

    /* *******************************************************************************
     ** 메뉴얼관리(관리자용) 프로그램 list 부분
     ** ******************************************************************************* */

    @PostMapping("/sys4010_list")
    public String sys4010_list(HttpServletRequest request) throws IOException {
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
            result = sysService.selectQryList("sys4010_list", reqParam);

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


    @PostMapping("/sys4010_docA_select")
    public String sys4010_docA_select(HttpServletRequest request) throws IOException {
        System.out.println("sys4010_docA_select.........");
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
            result = sysService.selectQryList("sys4010_docA_select", reqParam);

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


    @PostMapping("/sys4010_docA_save")
    public String sys4010_docA_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
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
                    int rtnI = sysService.insertQry("sys4010_docA_insert", param);
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
                    int rtnU = sysService.updateQry("sys4010_docA_update", param);
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
                    int rtnD = sysService.deleteQry("sys4010_docA_delete", param);
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
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }

    /* *******************************************************************************
     ** 메뉴얼관리(사용자공통) 프로그램 list 부분
     ** ******************************************************************************* */

    @PostMapping("/sys4020_list")
    public String sys4020_list(HttpServletRequest request) throws IOException {
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
            result = sysService.selectQryList("sys4020_list", reqParam);

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

    @PostMapping("/sys4020_docB_select")
    public String sys4020_docB_select(HttpServletRequest request) throws IOException {
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
            result = sysService.selectQryList("sys4020_docB_select", reqParam);

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


    @PostMapping("/sys4020_docB_save")
    public String sys4020_docB_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
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
                    int rtnI = sysService.insertQry("sys4020_docB_insert", param);
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
                    int rtnU = sysService.updateQry("sys4020_docB_update", param);
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
                    int rtnD = sysService.deleteQry("sys4020_docB_delete", param);
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
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }

    /* *******************************************************************************
     ** 메뉴얼관리(사용자전통) 프로그램 list 부분
     ** ******************************************************************************* */

    @PostMapping("/sys4030_list")
    public String sys4030_list(HttpServletRequest request) throws IOException {
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
            result = sysService.selectQryList("sys4030_list", reqParam);

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

    @PostMapping("/sys4030_docU_select")
    public String sys4030_docU_select(HttpServletRequest request) throws IOException {
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
            result = sysService.selectQryList("sys4030_docU_select", reqParam);

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


    @PostMapping("/sys4030_docU_save")
    public String sys4030_docU_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
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
                    int rtnI = sysService.insertQry("sys4030_docU_insert", param);
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
                    int rtnU = sysService.updateQry("sys4030_docU_update", param);
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
                    int rtnD = sysService.deleteQry("sys4030_docU_delete", param);
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
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }


    /* *******************************************************************************
     ** 메뉴정보 그룹 list 부분
     ** ******************************************************************************* */
//    @RequestMapping(value="/sys5010_group_list", produces="application/json; charset=utf8" , method = RequestMethod.POST)
    @PostMapping("/sys5010_group_list")
    public String sys5010_group_list(HttpServletRequest request) throws IOException {
        System.out.println("sys5010_group_list.........");
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
            result = sysService.selectQryList("sys5010_group_list", reqParam);

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
//    @RequestMapping(value="/sys5010_menu_list", produces="application/json; charset=utf8" , method = RequestMethod.POST)
    @PostMapping("/sys5010_menu_list")
    public String sys5010_menu_list(HttpServletRequest request) throws IOException {
        System.out.println("sys5010_menu_list.........");
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
            result = sysService.selectQryList("sys5010_menu_list", reqParam);

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

    @PostMapping("/sys5010_save")
    public String sys5010_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
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
                    int rtnI = sysService.insertQry("sys5010_insert", param);
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
                    int rtnU = sysService.updateQry("sys5010_update", param);
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
                    int rtnD = sysService.deleteQry("sys5010_delete", param);
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
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }

    /* *******************************************************************************
     ** 메뉴설정 프로그램 list 부분
     ** ******************************************************************************* */
//    @RequestMapping(value="/sys5020_prog_list", produces="application/json; charset=utf8" , method = RequestMethod.POST)
    @PostMapping("/sys5020_prog_list")
    public String sys5020_prog_list(HttpServletRequest request) throws IOException {
        System.out.println("sys5020_prog_list.........");
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
            result = sysService.selectQryList("sys5020_prog_list", reqParam);

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
//    @RequestMapping(value="/sys5020_menu_list", produces="application/json; charset=utf8" , method = RequestMethod.POST)
    @PostMapping("/sys5020_menu_list")
    public String sys5020_menu_list(HttpServletRequest request) throws IOException {
        System.out.println("sys5020_menu_list.........");
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
            result = sysService.selectQryList("sys5020_menu_list", reqParam);

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

    @PostMapping("/sys5020_save")
    public String sys5020_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
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
                    int rtn1 = sysService.deleteQry("sys5020_delete", param);
                    System.out.println("=====>>>  " + rtn1);
                    divde = divde_D;
                }

                if (!divde_I.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    sysService.insertQry("sys5020_insert", param);
                    divde = divde_I;
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    sysService.updateQry("sys5020_update", param);
                    divde = divde_U;
                }
            }
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            rtn = "1";
            e.printStackTrace();
        }
        map.put("rtn", rtn);
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");

        return jsonDataRtn;
    }

    @PostMapping("/sys5020_select_delete")
    public String sys5020_select_delete(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {

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
                    int rtn1 = sysService.deleteQry("sys5020_select_delete", param);
                    System.out.println("=====>>>  " + rtn1);
                    divde = divde_D;
                }

            }
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            rtn = "1";
            e.printStackTrace();
        }
        map.put("rtn", rtn);
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");

        return jsonDataRtn;
    }

    /* *******************************************************************************
     ** 즐겨찾기 메뉴설정 선택된 메뉴 list 부분
     ** ******************************************************************************* */
    @PostMapping("/sys5030_fav_menu_list")
    public String sys5030_fav_menu_list(HttpServletRequest request) throws IOException {
        System.out.println("sys5030_fav_menu_list.........");
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
            result = sysService.selectQryList("sys5030_fav_menu_list", reqParam);

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

    @PostMapping("/sys5030_fav_save")
    public String sys5030_fav_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
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
                    int rtn1 = sysService.deleteQry("sys5030_fav_delete", param);
                    System.out.println("=====>>>  " + rtn1);
                    divde = divde_D;
                }

                if (!divde_I.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    sysService.insertQry("sys5030_fav_insert", param);
                    divde = divde_I;
                }

                if (!divde_U.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_U);
                    param.put("userId", userInfo.getName());
                    sysService.updateQry("sys5030_fav_update", param);
                    divde = divde_U;
                }
            }
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            rtn = "1";
            e.printStackTrace();
        }
        map.put("rtn", rtn);
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");

        return jsonDataRtn;
    }

    @PostMapping("/sys5030_fav_select_delete")
    public String sys5030_fav_select_delete(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {

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
                    int rtn1 = sysService.deleteQry("sys5030_fav_select_delete", param);
                    System.out.println("=====>>>  " + rtn1);
                    divde = divde_D;
                }

            }
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            rtn = "1";
            e.printStackTrace();
        }
        map.put("rtn", rtn);
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");

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

            result = sysService.selectQryOne1("passwordCheck", reqParam);
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
        TransactionStatus status = transactionManager.getTransaction(def);
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
                    int rtnU = sysService.updateQry("passwdUpdate_save", param);
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
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }

}
