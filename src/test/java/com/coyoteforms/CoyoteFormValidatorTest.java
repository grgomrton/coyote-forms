package com.coyoteforms;

import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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

    private static CoyoteFormValidator<LocationDto> validator;

    @BeforeAll
    public static void init() {
        validator = new CoyoteFormValidator<>(ruleSet, new CountryAndCityConnector());

    }

    @ParameterizedTest
    @MethodSource("validSelections")
    public void validatorShouldAcceptValidInput(LocationDto selectedLocation) {
        assertThat(validator.validate(selectedLocation)).isEmpty();
    }

    private static Stream<Arguments> validSelections() {
        return Stream.of(
                Arguments.of(LocationDto.builder().country("Hungary").city("Budapest").build()),
                Arguments.of(LocationDto.builder().country("United Kingdom").city("London").build()),
                // we assume that the parser during an http call will set empty string into non-present input field
                Arguments.of(LocationDto.builder().country("United Kingdom").city("").build())
        );
    }

    @Data
    @Builder
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
