package com.coyoteforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class CoyoteFormValidatorTest {

    //Planned input:
    //  {
    //    "rules": [
    //      {
    //        "inputId": "country",
    //        "condition": [ "always" ],
    //        "permittedValues": ["United Kingdom", "Hungary"]
    //      },
    //      {
    //        "inputId": "city",
    //        "condition": [ "country is 'Hungary'" ],
    //        "permittedValues": ["Budapest", "Sopron"]
    //      },
    //      {
    //        "inputId": "city",
    //        "condition": [ "country is 'United Kingdom'" ],
    //        "permittedValues": ["London"]
    //      }
    //    ]
    //  }

    private static String ruleSet = "  {\n" +
            "  \"rules\": [\n" +
            "    {\n" +
            "    \"inputId\": \"country\",\n" +
            "    \"condition\": [ \"always\" ],\n" +
            "    \"permittedValues\": [\"United Kingdom\", \"Hungary\"]\n" +
            "    },\n" +
            "    {\n" +
            "    \"inputId\": \"city\",\n" +
            "    \"condition\": [ \"country is 'Hungary'\" ],\n" +
            "    \"permittedValues\": [\"Budapest\", \"Sopron\"]\n" +
            "    },\n" +
            "    {\n" +
            "    \"inputId\": \"city\",\n" +
            "    \"condition\": [ \"country is 'United Kingdom'\" ],\n" +
            "    \"permittedValues\": [\"London\"]\n" +
            "    }\n" +
            "  ]\n" +
            "}\n";

    @Test
    public void validatorShouldParseInputRuleSet() throws JsonProcessingException {
        new CoyoteFormValidator<>(ruleSet, new CountryAndCityConnector());
    }

    @Data
    public static class LocationDto {

        private String country;

        private String city;

    }

    public static class CountryAndCityConnector implements Connector<LocationDto> {

        @Override
        public Map<String, String> collectInputValues(LocationDto locationDto) {
            Map<String, String> result = new HashMap<>();

            if (!locationDto.country.isEmpty()) {
                result.put("country", locationDto.country);
            }
            if (!locationDto.city.isEmpty()) {
                result.put("city", locationDto.city);
            }

            return result;
        }
    }
}
