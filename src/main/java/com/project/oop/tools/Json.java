package com.project.oop.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Json {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static ObjectMapper getDefaultObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }

    public static JsonNode toJson(String json) throws JsonProcessingException {
        return objectMapper.readTree(json);

    }

    public static <A> A fromJson(JsonNode json, Class<A> clazz) throws JsonProcessingException {
        return objectMapper.treeToValue(json, clazz );
    }

    public static ObjectNode createNode(){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode responseNode = objectMapper.createObjectNode();
        return responseNode;
    }
}
