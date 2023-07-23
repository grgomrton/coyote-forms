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

public class CoyoteFormsValidatorValidateAdditiveRulesTest {

    private static String RULE_SET = "    {" +
            "      \"constraints\": [" +
            "        {" +
            "          \"inputIds\": [ \"franceCheckbox\" ]," +
            "          \"condition\": [ \"always\" ]," +
            "          \"permittedValues\": [ \"checked\", \"unchecked\" ]" +
            "        }," +
            "        {" +
            "          \"inputIds\": [ \"germanyCheckbox\" ]," +
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

    private static CoyoteFormsValidator<SelectedCountriesAndCityDto> validator;

    @BeforeAll
    public static void init() {
        validator = new CoyoteFormsValidator<>(RULE_SET, new EveryValuePassingConnector());
    }

    @Data
    @Builder
    public static class SelectedCountriesAndCityDto {

        private List<String> selectedCountryNames;

        private String city;

    }

    public static class EveryValuePassingConnector implements Connector<SelectedCountriesAndCityDto> {

        @Override
        public Map<String, String> collectInputValues(SelectedCountriesAndCityDto selectedCountriesAndCityDto) {
            Map<String, String> result = new HashMap<>();
            List<String> selectedCountryNames = selectedCountriesAndCityDto.getSelectedCountryNames();

            result.put("franceCheckbox", selectedCountryNames.contains("France") ? "checked" : "unchecked");
            result.put("germanyCheckbox", selectedCountryNames.contains("Germany") ? "checked" : "unchecked");

            result.put("city", selectedCountriesAndCityDto.getCity());

            return result;
        }
    }

    @ParameterizedTest
    @MethodSource("invalidSelections")
    public void validatorShouldCatchInvalidInput(SelectedCountriesAndCityDto selection, List<String> invalidInputIds) {
        assertThat(collectInputIds(validator.validate(selection))).containsExactlyInAnyOrderElementsOf(invalidInputIds);
    }

    @ParameterizedTest
    @MethodSource("validSelections")
    public void validatorShouldPassValidInput(SelectedCountriesAndCityDto selection) {
        assertThat(validator.validate(selection)).isEmpty();
    }

    private static Stream<Arguments> invalidSelections() {
        return Stream.of(
                Arguments.of(SelectedCountriesAndCityDto.builder()
                                .selectedCountryNames(List.of("France"))
                                .city("Berlin").build(),
                        List.of("city")),
                Arguments.of(SelectedCountriesAndCityDto.builder()
                                .selectedCountryNames(List.of())
                                .city("").build(),
                        List.of("city")),
                Arguments.of(SelectedCountriesAndCityDto.builder()
                                .selectedCountryNames(List.of("France", "Germany"))
                                .city("Budapest")
                                .build(),
                        List.of("city"))
        );
    }

    private static Stream<Arguments> validSelections() {
        return Stream.of(
                Arguments.of(SelectedCountriesAndCityDto.builder()
                                .selectedCountryNames(List.of("Germany"))
                                .city("Munich")
                                .build()
                ),
                Arguments.of(SelectedCountriesAndCityDto.builder()
                                .selectedCountryNames(List.of("Germany", "France"))
                                .city("Paris").build()
                ),
                Arguments.of(SelectedCountriesAndCityDto.builder()
                        .selectedCountryNames(List.of("Germany", "France"))
                        .city("Berlin").build()
                )
        );
    }

}
