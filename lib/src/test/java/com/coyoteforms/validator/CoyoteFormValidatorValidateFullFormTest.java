package com.coyoteforms.validator;

import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class CoyoteFormValidatorValidateFullFormTest {

    private static String RULE_SET = "  {" +
            "  \"rules\": [" +
            "    {" +
            "    \"inputId\": \"country\"," +
            "    \"condition\": [ \"always\" ]," +
            "    \"permittedValues\": [\"United Kingdom\", \"Hungary\"]" +
            "    }," +
            "    {" +
            "    \"inputId\": \"city\"," +
            "    \"condition\": [ \"country is 'Hungary'\" ]," +
            "    \"permittedValues\": [\"Budapest\", \"Sopron\"]" +
            "    }," +
            "    {" +
            "    \"inputId\": \"city\"," +
            "    \"condition\": [ \"country is 'United Kingdom'\" ]," +
            "    \"permittedValues\": [\"London\"]" +
            "    }" +
            "  ]" +
            "}";

    private static CoyoteFormValidator<LocationDto> validator;

    @BeforeAll
    public static void init() {
        validator = new CoyoteFormValidator<>(RULE_SET, new FullFormPassingConnector());
    }

    @Data
    @Builder
    public static class LocationDto {

        private String country;

        private String city;

    }

    public static class FullFormPassingConnector implements Connector<LocationDto> {

        @Override
        public Map<String, String> collectInputValues(LocationDto locationDto) {
            Map<String, String> result = new HashMap<>();

            result.put("country", locationDto.country);
            result.put("city", locationDto.city);

            return result;
        }
    }

    @ParameterizedTest
    @MethodSource("validSelections")
    public void validatorShouldAcceptValidInput(LocationDto selectedLocation) {
        assertThat(validator.validate(selectedLocation)).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("invalidSelections")
    public void validatorShouldCatchInvalidInput(LocationDto selectedLocation, List<String> invalidInputIds) {
        assertThat(validator.validate(selectedLocation)).containsExactlyInAnyOrderElementsOf(invalidInputIds);
    }

    private static Stream<Arguments> validSelections() {
        return Stream.of(
                Arguments.of(LocationDto.builder().country("Hungary").city("Budapest").build()),
                Arguments.of(LocationDto.builder().country("United Kingdom").city("London").build())
        );
    }

    private static Stream<Arguments> invalidSelections() {
        return Stream.of(
                Arguments.of(LocationDto.builder().country("Hungary").city("Debrecen").build(), List.of("city")),
                Arguments.of(LocationDto.builder().country("United Kingdom").city("Glasgow").build(), List.of("city")),
                Arguments.of(LocationDto.builder().country("Abc").city("").build(), List.of("country", "city")),
                Arguments.of(LocationDto.builder().country("").city("Budapest").build(), List.of( "country", "city")),
                // this will now fail, because one key has not-allowed value (city is empty)
                Arguments.of(LocationDto.builder().country("United Kingdom").city("").build(), List.of("city")),
                Arguments.of(LocationDto.builder().country("").city("").build(), List.of("country", "city"))
        );
    }

}