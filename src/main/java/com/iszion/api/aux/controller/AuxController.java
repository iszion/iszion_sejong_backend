package com.iszion.api.aux.controller;

import com.iszion.api.comn.DataRequestUtil;
import com.iszion.api.comn.JsonUtils;
import com.iszion.api.comn.RequestUtil;
import com.iszion.api.config.jwt.JwtTokenProvider;
import com.iszion.api.aux.service.AuxService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.*;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

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
@RequestMapping("/api/aux")
public class AuxController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuxController.class);
    private final AuxService auxService;
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
     ** 전체자료정리 작업
     ** ******************************************************************************* */
    @PostMapping("/aux1010_procedure")
    public String aux1010_procedure(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {
        String accessToken = token.substring(7);
        Authentication userInfo = jwtTokenProvider.getAuthentication(accessToken);
        String userId = userInfo.getName();

        RequestUtil reqUtil = new RequestUtil();
        String jsonData = reqUtil.getBody(request);
        JsonUtils jsonUtil = new JsonUtils();
        Map<String, Object> reqParam = jsonUtil.jsonStringToMap(jsonData);
        String pDayFrom = (String) reqParam.get("paramDayFrom");
        String pDayTo = (String) reqParam.get("paramDayTo");

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
            cs = con.prepareCall("{call PROC_STOCK_COMP(?, ?, ?, ?)}");

            // 입력 파라미터 설정
            cs.setString(1, pDayFrom);
            cs.setString(2, pDayTo);
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

}
