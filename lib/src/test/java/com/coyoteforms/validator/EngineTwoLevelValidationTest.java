package com.coyoteforms.validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class EngineTwoLevelValidationTest {

    private static Engine engine;

    @BeforeAll
    public static void init() {
        List<Rule> rules = List.of(
                Rule.builder()
                        .inputIds(List.of("country"))
                        .condition(List.of("always"))
                        .permittedValues(List.of("United Kingdom", "Hungary"))
                        .build(),
                Rule.builder()
                        .inputIds(List.of("city"))
                        .condition(List.of("country is Hungary"))
                        .permittedValues(List.of("Budapest", "Sopron"))
                        .build(),
                Rule.builder()
                        .inputIds(List.of("city"))
                        .condition(List.of("country is United Kingdom"))
                        .permittedValues(List.of("London"))
                        .build());
        engine = new Engine(rules);
    }

    @Test
    public void engineShouldValidateSubsetOfValuesDefinedByRules() {
        Map<String, String> validSelection = Map.of("country", "United Kingdom");

        Map<String, Set<String>> validationResult = engine.validateInput(validSelection);

        assertThat(validationResult).isEmpty();
    }

    @Test
    public void engineShouldPermitValidSelection() {
        Map<String, String> validSelection = Map.of("country", "United Kingdom", "city", "London");

        Map<String, Set<String>> validationResult = engine.validateInput(validSelection);

        assertThat(validationResult).isEmpty();
    }

    @Test
    public void engineShouldMarkBothLevelInvalidInCaseInvalidSelectionOnFirstLevel() {
        Map<String, String> validSelection = Map.of("country", "France", "city", "Paris");

        Map<String, Set<String>> validationResult = engine.validateInput(validSelection);

        assertThat(collectInputIds(validationResult)).containsExactlyInAnyOrder("country", "city");
    }


    @Test
    public void engineShouldMarkSecondLevelInvalidInCaseInvalidSelectionOnSecondLevel() {
        Map<String, String> validSelection = Map.of("country", "United Kingdom", "city", "Paris");

        Map<String, Set<String>> validationResult = engine.validateInput(validSelection);

        assertThat(collectInputIds(validationResult)).containsExactlyInAnyOrder("city");
    }

    private static Set<String> collectInputIds(Map<String, Set<String>> validationResult) {
        return validationResult.keySet();
    }

}
