package com.iszion.api.comn;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author TIAGO.MEDICI
 * 
 */
public class JsonUtils {

    public boolean isJSONValid(String jsonInString) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String serializeAsJsonString(Object object) throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objMapper = new ObjectMapper();
        objMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        StringWriter sw = new StringWriter();
        objMapper.writeValue(sw, object);
        return sw.toString();
    }

    public String serializeAsJsonString(Object object, boolean indent) throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objMapper = new ObjectMapper();
        if (indent == true) {
            objMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        }

        StringWriter stringWriter = new StringWriter();
        objMapper.writeValue(stringWriter, object);
        return stringWriter.toString();
    }

    public <T> T jsonStringToObject(String content, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
        T obj = null;
        ObjectMapper objMapper = new ObjectMapper();
        obj = objMapper.readValue(content, clazz);
        return obj;
    }

    @SuppressWarnings("rawtypes")
    public <T> T jsonStringToObjectArray(String content) throws JsonParseException, JsonMappingException, IOException {
        T obj = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        obj = (T) mapper.readValue(content, new TypeReference<List>() {
        });
        return obj;
    }

    public <T> T jsonStringToObjectArray(String content, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
        T obj = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper = new ObjectMapper().configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        obj = mapper.readValue(content, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        return obj;
    }
    
    public <T> T jsonStringToMap(String content) throws JsonParseException, JsonMappingException, IOException {
        T obj = null;
        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> map = new HashMap<String, Object>();
        obj = (T) mapper.readValue(content, new TypeReference<Map<String, Object>>(){});
        return obj;
    }

    public String getToJson(Object obj)
    {
    	Gson gson = new Gson();
    	GsonBuilder builder = new GsonBuilder();
    	gson = builder.serializeNulls().create();

    	
	    String JSONObject = gson.toJson(obj);
	    return JSONObject;
    }
  
}