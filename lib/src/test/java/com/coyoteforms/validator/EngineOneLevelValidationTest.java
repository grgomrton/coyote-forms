package com.coyoteforms.validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class EngineOneLevelValidationTest {

    private static Engine engine;

    @BeforeAll
    public static void init() {
        List<Rule> rules = List.of(
                Rule.builder()
                        .inputIds(List.of("country"))
                        .condition(List.of("always"))
                        .permittedValues(List.of("United Kingdom", "Hungary"))
                        .build());
        engine = new Engine(rules);
    }

    @Test
    public void engineShouldAllowPermittedValueOfAlwaysConditionRule() {
        Map<String, String> validSelection = Map.of("country", "United Kingdom");

        Map<String, Set<String>> invalidIds = engine.validateInput(validSelection);

        assertThat(invalidIds).isEmpty();
    }

    @Test
    public void engineShouldMarkNonPermittedValueOfAlwaysConditionRule() {
        Map<String, String> invalidSelection = Map.of("country", "France");

        Map<String, Set<String>> invalidIds = engine.validateInput(invalidSelection);

        assertThat(invalidIds.keySet()).containsExactlyInAnyOrder("country");
    }

}
