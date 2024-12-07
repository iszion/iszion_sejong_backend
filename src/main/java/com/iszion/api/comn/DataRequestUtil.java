package com.iszion.api.comn;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class DataRequestUtil {
    public Map getParameterMap(HttpServletRequest request){
        Map parameterMap = new HashMap();
        Enumeration enums = request.getParameterNames();
        while(enums.hasMoreElements()){
            String paramName = (String)enums.nextElement();
            String[] parameters = request.getParameterValues(paramName);

            // Parameter가 배열일 경우
            if(parameters.length > 1){
                parameterMap.put(paramName, parameters);
                // Parameter가 배열이 아닌 경우
            }else{
                parameterMap.put(paramName, parameters[0]);
            }
        }
        return parameterMap;
    }

    // json 받기
    public String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }
    public String rtnJson()
    {
        return 	"{\"records\":[{\"ID\":1,\"Name\":\"Hristo Stoichkov\",\"PlaceOfBirth\":\"Plovdiv, Bulgaria\",\"DateOfBirth\":\"/Date(-122227200000)/\",\"CountryID\":18,\"CountryName\":\"Bulgaria\",\"IsActive\":false,\"OrderNumber\":1},{\"ID\":2,\"Name\":\"Ronaldo Luis Nazario de Lima\",\"PlaceOfBirth\":\"Rio de Janeiro, Brazil\",\"DateOfBirth\":\"/Date(211129200000)/\",\"CountryID\":6,\"CountryName\":\"Brazil\",\"IsActive\":false,\"OrderNumber\":2},{\"ID\":3,\"Name\":\"David Platt\",\"PlaceOfBirth\":\"Chadderton, United Kingdom\",\"DateOfBirth\":\"/Date(-112122000000)/\",\"CountryID\":16,\"CountryName\":\"England\",\"IsActive\":false,\"OrderNumber\":3},{\"ID\":4,\"Name\":\"Manuel Neuer\",\"PlaceOfBirth\":\"Gelsenkirchen, West Germany\",\"DateOfBirth\":\"/Date(512294400000)/\",\"CountryID\":17,\"CountryName\":\"Germany\",\"IsActive\":true,\"OrderNumber\":4},{\"ID\":5,\"Name\":\"James Rodriguez\",\"PlaceOfBirth\":\"Cucuta, Colombia\",\"DateOfBirth\":\"/Date(679302000000)/\",\"CountryID\":14,\"CountryName\":\"Colombia\",\"IsActive\":true,\"OrderNumber\":5}],\"total\":7}";
    }
 }
