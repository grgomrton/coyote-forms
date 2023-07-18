package com.coyoteforms.validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class EngineOneLevelValidationTest {

    //Planned input:
    //
    //  [
    //  {
    //    "inputId": "country",
    //    "condition": [ "always" ],
    //    "permittedValues": ["United Kingdom", "Hungary"]
    //  }
    //  ]

    private static Engine engine;

    @BeforeAll
    public static void init() {
        List<DiscreteRule> rules = List.of(
                DiscreteRule.builder()
                        .inputId("country")
                        .condition(List.of("always"))
                        .permittedValues(List.of("United Kingdom", "Hungary"))
                        .build());
        engine = new Engine(RuleSet.builder().discreteValueRules(rules).build());
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
