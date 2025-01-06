package com.iszion.api.coup.controller;

import com.iszion.api.comn.DataRequestUtil;
import com.iszion.api.comn.JsonUtils;
import com.iszion.api.comn.RequestUtil;
import com.iszion.api.config.jwt.JwtTokenProvider;
import com.iszion.api.coup.service.CoupService;
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
@RequestMapping("/api/coup")
public class CoupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoupController.class);
    private final CoupService coupService;
    private final SalService salService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PlatformTransactionManager transactionManager;  // 트랜잭션 매니저


//*************************************************************************************************
//***********   sal2020  (코업 출고접수 쿼리)정보 DB SQL    *********************************************
//*************************************************************************************************
    @PostMapping("/sal2020_coup_select_list")
    public String sal2020_coup_select_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = coupService.selectQryList("sal2020_coup_select_list", reqParam);
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



    @PostMapping("/sal2020_coup_save")
    public String sal2020_coup_save(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
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
                if (!divde_I.isEmpty()) {
                    Map param = new HashMap();
                    param.put("list1", divde_I);
                    param.put("userId", userInfo.getName());
                    int rtnI = coupService.insertQry("sal2020_coup_insert", param);
                    if(rtnI > 0) {
                        rtnI = salService.updateQry("sal2020_update_accept", param);
                    }
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

//*************************************************************************************************
//***********   sal2030  (코업 반품전표  쿼리)정보 DB SQL    ********************************************
//*************************************************************************************************
    @PostMapping("/sal2030_coup_return_day_list")
    public String sal2030_coup_return_day_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = coupService.selectQryList("sal2030_coup_return_day_list", reqParam);
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

    @PostMapping("/sal2030_coup_return_list")
    public String sal2030_coup_return_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = coupService.selectQryList("sal2030_coup_return_list", reqParam);
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

    @PostMapping("/sal2030_select_return_list")
    public String sal2030_select_return_list(HttpServletRequest request, @RequestHeader("Authorization") String token) throws IOException {
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
            result = coupService.selectQryList("sal2030_select_return_list", reqParam);
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

}
