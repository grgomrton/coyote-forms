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

import static com.coyoteforms.validator.TestUtilities.collectInputIds;
import static org.assertj.core.api.Assertions.assertThat;

public class CoyoteFormValidatorValidateOnlySelectedValuesTest {

    private static String RULE_SET = "  {" +
            "  \"constraints\": [" +
            "    {" +
            "    \"inputIds\": [ \"country\" ]," +
            "    \"condition\": [ \"always\" ]," +
            "    \"permittedValues\": [\"United Kingdom\", \"Hungary\"]" +
            "    }," +
            "    {" +
            "    \"inputIds\": [ \"city\" ]," +
            "    \"condition\": [ \"country is Hungary\" ]," +
            "    \"permittedValues\": [\"Budapest\", \"Sopron\"]" +
            "    }," +
            "    {" +
            "    \"inputIds\": [ \"city\" ]," +
            "    \"condition\": [ \"country is United Kingdom\" ]," +
            "    \"permittedValues\": [\"London\"]" +
            "    }" +
            "  ]" +
            "}";

    private static CoyoteFormValidator<LocationDto> validator;

    @BeforeAll
    public static void init() {
        validator = new CoyoteFormValidator<>(RULE_SET, new OnlySelectedValuesPassingConnector());
    }

    @Data
    @Builder
    public static class LocationDto {

        private String country;

        private String city;

    }

    public static class OnlySelectedValuesPassingConnector implements Connector<LocationDto> {

        @Override
        public Map<String, String> collectInputValues(LocationDto locationDto) {
            Map<String, String> result = new HashMap<>();

            if (!locationDto.country.isEmpty()) { // we add the value of the input only if it is set
                result.put("country", locationDto.country);
            }
            if (!locationDto.city.isEmpty()) {
                result.put("city", locationDto.city);
            }

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
        assertThat(collectInputIds(validator.validate(selectedLocation))).containsExactlyInAnyOrderElementsOf(invalidInputIds);
    }

    private static Stream<Arguments> validSelections() {
        return Stream.of(
                Arguments.of(LocationDto.builder().country("Hungary").city("Budapest").build()),
                Arguments.of(LocationDto.builder().country("United Kingdom").city("London").build()),
                // we assume that the connector will set empty string into non-present input field
                Arguments.of(LocationDto.builder().country("United Kingdom").city("").build()), // todo so why is this valid?
                // we again expect empty string in case of missing input
                Arguments.of(LocationDto.builder().country("").city("").build())
        );
    }

    private static Stream<Arguments> invalidSelections() {
        return Stream.of(
                Arguments.of(LocationDto.builder().country("Hungary").city("Debrecen").build(), List.of("city")),
                Arguments.of(LocationDto.builder().country("United Kingdom").city("Glasgow").build(), List.of("city")),
                Arguments.of(LocationDto.builder().country("Abc").city("").build(), List.of("country")),
                Arguments.of(LocationDto.builder().country("").city("Budapest").build(), List.of( "city"))
        );
    }

}
