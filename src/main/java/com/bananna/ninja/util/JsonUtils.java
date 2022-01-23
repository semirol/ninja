package com.bananna.ninja.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T parse(String s, Class<T> clazz) {
        try {
            return objectMapper.readValue(s, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String stringify(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T mapToPOJO(Map<String, Object> map, Class<T> clazz) {
        String result = stringify(map);
        return parse(result, clazz);
    }

    public static Map POJOToMap(Object o){
        String result = stringify(o);
        return parse(result, Map.class);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

}
