package com.coyoteforms;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class EngineTwoLevelValidationTest {

    //Planned input:
    //
    //  [
    //  {
    //    "inputId": "country",
    //    "condition": [ "always" ],
    //    "permittedValues": ["United Kingdom", "Hungary"]
    //  },
    //  {
    //    "inputId": "city",
    //    "condition": [ "country is 'Hungary'" ],
    //    "permittedValues": ["Budapest", "Sopron"]
    //  },
    //  {
    //    "inputId": "city",
    //    "condition": [ "country is 'United Kingdom'" ],
    //    "permittedValues": ["London"]
    //  }
    //  ]

    private static Engine engine;

    @BeforeAll
    private static void init() {
        List<Rule> rules = List.of(
                Rule.builder()
                        .inputId("country")
                        .condition(List.of("always"))
                        .permittedValues(List.of("United Kingdom", "Hungary"))
                        .build(),
                Rule.builder()
                        .inputId("city")
                        .condition(List.of("country is 'Hungary'"))
                        .permittedValues(List.of("Budapest", "Sopron"))
                        .build(),
                Rule.builder()
                        .inputId("city")
                        .condition(List.of("country is 'United Kingdom'"))
                        .permittedValues(List.of("London"))
                        .build());
        engine = new Engine(rules);
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
