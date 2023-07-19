package com.coyoteforms.validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class EngineThreeLevelValidationTest {

    private static Engine engine;

    @BeforeAll
    public static void init() {
        List<DiscreteRule> rules = List.of(
                DiscreteRule.builder()
                        .inputId("region")
                        .condition(List.of("always"))
                        .permittedValues(List.of("Americas", "EMEA"))
                        .build(),
                DiscreteRule.builder()
                        .inputId("country")
                        .condition(List.of("region is 'Americas'"))
                        .permittedValues(List.of("U.S.A.", "Mexico"))
                        .build(),
                DiscreteRule.builder()
                        .inputId("country")
                        .condition(List.of("region is 'EMEA'"))
                        .permittedValues(List.of("United Kingdom", "Hungary"))
                        .build(),
                DiscreteRule.builder()
                        .inputId("city")
                        .condition(List.of("region is 'Americas'", "country is 'U.S.A.'"))
                        .permittedValues(List.of("New York", "Washington"))
                        .build(),
                DiscreteRule.builder()
                        .inputId("city")
                        .condition(List.of("region is 'Americas'", "country is 'Mexico'"))
                        .permittedValues(List.of("Toluca de Lerdo"))
                        .build(),
                DiscreteRule.builder()
                        .inputId("city")
                        .condition(List.of("region is 'EMEA'", "country is 'Hungary'"))
                        .permittedValues(List.of("Budapest", "Sopron"))
                        .build(),
                DiscreteRule.builder()
                        .inputId("city")
                        .condition(List.of("region is 'EMEA'", "country is 'United Kingdom'"))
                        .permittedValues(List.of("London"))
                        .build());
        engine = new Engine(RuleSet.builder().rules(rules).build());
    }

    @ParameterizedTest
    @MethodSource("validSelections")
    public void engineShouldEvaluateMultipleConditionsForEntry(Map<String, String> inputValues) {
        assertThat(engine.validateInput(inputValues)).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("invalidSelections")
    public void engineShouldEvaluateMultipleConditionsForEntry(Map<String, String> inputValues, List<String> invalidInputIds) {
        assertThat(engine.validateInput(inputValues)).containsExactlyInAnyOrderElementsOf(invalidInputIds);
    }

    private static Stream<Arguments> validSelections() {
        return Stream.of(
                Arguments.of(Map.of("region", "Americas", "country", "U.S.A.", "city", "Washington")),
                Arguments.of(Map.of("region", "EMEA", "country", "Hungary", "city", "Sopron")),
                Arguments.of(Map.of("region", "Americas", "country", "Mexico"))
        );
    }

    private static Stream<Arguments> invalidSelections() {
        return Stream.of(
                Arguments.of(
                        Map.of("region", "Americas", "country", "Hungary", "city", "Sopron"),
                        List.of("country", "city")),
                Arguments.of(
                        Map.of("region", "EMEA", "country", "Netherlands"),
                        List.of("country"))
        );
    }

}
