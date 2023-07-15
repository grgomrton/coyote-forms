package com.coyoteforms;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class EngineOneLevelValidationTest {

    //Imaginary input:
    //
    //  [
    //  {
    //    "inputId": "country",
    //    "condition": "always",
    //    "permittedValues": ["United Kingdom", "Hungary"]
    //  }
    //  ]

    private static Engine engine;

    @BeforeAll
    private static void init() {
        List<Rule> rules = List.of(
                Rule.builder()
                        .inputId("country")
                        .condition("always")
                        .permittedValues(List.of("United Kingdom", "Hungary"))
                        .build());
        engine = new Engine(rules);
    }

    @Test
    public void engineShouldAllowPermittedValueOfAlwaysConditionRule() {
        Map<String, String> validSelection = Map.of("country", "United Kingdom");

        List<String> invalidIds = engine.validateInput(validSelection);

        assertThat(invalidIds).isEmpty();
    }

    @Test
    public void engineShouldMarkNonPermittedValueOfAlwaysConditionRule() {
        Map<String, String> validSelection = Map.of("country", "France");

        List<String> invalidIds = engine.validateInput(validSelection);

        assertThat(invalidIds).containsExactlyInAnyOrder("country");
    }

}
