package com.vieira.sogolon.ItauChallenge.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonTest {

    String simpleTestCaseUserRole = "{ \"userRole\": \"BASIC\"}";

    @Test
    void parse() throws JsonProcessingException {

        JsonNode node = Json.parse(simpleTestCaseUserRole);
        assertEquals("BASIC", node.get("userRole").asText());
    }
}