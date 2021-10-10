package com.xx.log.common.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class MixUtil {

    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @SneakyThrows
    public static String toJsonString(Object obj) {
        return objectMapper.writeValueAsString(obj);
    }

    @SneakyThrows
    public static <T> T jsonStr2Obj(String data, Class<T> clazz) {
        return objectMapper.readValue(data, clazz);
    }
}
