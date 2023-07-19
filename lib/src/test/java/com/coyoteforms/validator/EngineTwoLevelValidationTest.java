package com.coyoteforms.validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class EngineTwoLevelValidationTest {

    private static Engine engine;

    @BeforeAll
    public static void init() {
        List<DiscreteRule> rules = List.of(
                DiscreteRule.builder()
                        .inputId("country")
                        .condition(List.of("always"))
                        .permittedValues(List.of("United Kingdom", "Hungary"))
                        .build(),
                DiscreteRule.builder()
                        .inputId("city")
                        .condition(List.of("country is 'Hungary'"))
                        .permittedValues(List.of("Budapest", "Sopron"))
                        .build(),
                DiscreteRule.builder()
                        .inputId("city")
                        .condition(List.of("country is 'United Kingdom'"))
                        .permittedValues(List.of("London"))
                        .build());
        engine = new Engine(RuleSet.builder().rules(rules).build());
    }

    @Test
    public void engineShouldValidateSubsetOfValuesDefinedByRules() {
        Map<String, String> validSelection = Map.of("country", "United Kingdom");

        List<String> invalidIds = engine.validateInput(validSelection);

        assertThat(invalidIds).isEmpty();
    }

    @Test
    public void engineShouldPermitValidSelection() {
        Map<String, String> validSelection = Map.of("country", "United Kingdom", "city", "London");

        List<String> invalidIds = engine.validateInput(validSelection);

        assertThat(invalidIds).isEmpty();
    }

    @Test
    public void engineShouldMarkBothLevelInvalidInCaseInvalidSelectionOnFirstLevel() {
        Map<String, String> validSelection = Map.of("country", "France", "city", "Paris");

        List<String> invalidIds = engine.validateInput(validSelection);

        assertThat(invalidIds).containsExactlyInAnyOrder("country", "city");
    }


    @Test
    public void engineShouldMarkSecondLevelInvalidInCaseInvalidSelectionOnSecondLevel() {
        Map<String, String> validSelection = Map.of("country", "United Kingdom", "city", "Paris");

        List<String> invalidIds = engine.validateInput(validSelection);

        assertThat(invalidIds).containsExactlyInAnyOrder("city");
    }

}
