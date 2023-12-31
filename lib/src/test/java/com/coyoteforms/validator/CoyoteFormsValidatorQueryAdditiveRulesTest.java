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

public class CoyoteFormsValidatorQueryAdditiveRulesTest {

    private static String RULE_SET = "    {" +
            "      \"constraints\": [" +
            "        {" +
            "          \"inputIds\": [ \"franceCheckbox\", \"germanyCheckbox\" ]," +
            "          \"condition\": [ \"always\" ]," +
            "          \"permittedValues\": [ \"checked\", \"unchecked\" ]" +
            "        }," +
            "        {" +
            "          \"inputIds\": [ \"city\" ]," +
            "          \"condition\": [ \"franceCheckbox is checked\" ]," +
            "          \"permittedValues\": [ \"Paris\", \"Marseilles\" ]" +
            "        }," +
            "        {" +
            "          \"inputIds\": [ \"city\" ]," +
            "          \"condition\": [ \"germanyCheckbox is checked\" ]," +
            "          \"permittedValues\": [ \"Munich\", \"Berlin\", \"Hamburg\" ]" +
            "        }" +
            "      ]" +
            "    }";

    private static CoyoteFormsValidator<SelectedCountriesDto> validator;

    @BeforeAll
    public static void init() {
        validator = new CoyoteFormsValidator<>(RULE_SET, new EveryValuePassingConnector());
    }

    @Data
    @Builder
    static class SelectedCountriesDto {

        private List<String> selectedCountryNames;

    }

    public static class EveryValuePassingConnector implements Connector<SelectedCountriesDto> {

        @Override
        public Map<String, String> collectInputValues(SelectedCountriesDto selectedCountriesDto) {
            Map<String, String> result = new HashMap<>();
            List<String> selectedCountryNames = selectedCountriesDto.getSelectedCountryNames();

            result.put("franceCheckbox", selectedCountryNames.contains("France") ? "checked" : "unchecked");
            result.put("germanyCheckbox", selectedCountryNames.contains("Germany") ? "checked" : "unchecked");

            return result;
        }
    }

    @ParameterizedTest
    @MethodSource("selectedCountries")
    public void validatorShouldCollectValidInput(SelectedCountriesDto selectedCountries, List<String> possibleDestinationCities) {
        assertThat(validator.queryValidValues("city", selectedCountries)).containsExactlyInAnyOrderElementsOf(possibleDestinationCities);
    }

    private static Stream<Arguments> selectedCountries() {
        return Stream.of(
                Arguments.of(SelectedCountriesDto.builder().selectedCountryNames(
                        List.of("Germany")).build(),
                        List.of("Munich", "Berlin", "Hamburg")),
                Arguments.of(SelectedCountriesDto.builder().selectedCountryNames(
                        List.of()).build(),
                        List.of()),
                Arguments.of(SelectedCountriesDto.builder().selectedCountryNames(
                        List.of("France", "Germany")).build(),
                        List.of("Paris", "Marseilles", "Berlin", "Munich", "Hamburg"))
        );
    }


}
