package com.iszion.api.com.controller;

import com.iszion.api.com.service.ComDBService;
import com.iszion.api.comn.JsonUtils;
import com.iszion.api.comn.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/com")
public class ComDBController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComDBController.class);

    private  final ComDBService comDBService;

    public ComDBController(ComDBService comDBService) {
        this.comDBService = comDBService;
    }

    @PostMapping("/createDatabase")
    public String crateDatabase(HttpServletRequest request, @RequestHeader("Authorization") String token) throws Exception {

        RequestUtil reqUtil = new RequestUtil();
        String jsonData = reqUtil.getBody(request);
        JsonUtils jsonUtil = new JsonUtils();
        Map<String, Object> reqParam = jsonUtil.jsonStringToMap(jsonData);
        String dbName = (String) reqParam.get("paramDatabase");

        String jsonDataRtn = "";
        String rtn = "0";
        String rtnMsg = "";
        List<?> divde = null;
        Map<String, Object> map = new HashMap();

        try {
            comDBService.createDatabase(dbName);
            rtn = "0";
            rtnMsg = "Database [ '" + dbName + "' ] 생성 성공!!!";
        } catch (IllegalArgumentException e) {
            rtn = "1";
            rtnMsg = "데이터베이스 생성에 실패했습니다. : " + e.getMessage();
        } catch (Exception e) {
            rtn = "3";
            rtnMsg = "데이터베이스를 생성하는 동안 오류가 발생했습니다. : " + e.getMessage();
        }
        map.put("rtn", rtn);
        map.put("rtnMsg", rtnMsg);
        map.put("data", divde);
        jsonDataRtn = jsonUtil.getToJson(map).replaceAll("null", "\"\"");
        return jsonDataRtn;
    }
    /* *******************************************************************************
     ** 데이타베이스 생성 부분
     ** ******************************************************************************* */

    
}
