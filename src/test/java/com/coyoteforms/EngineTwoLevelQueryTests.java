package com.coyoteforms;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class EngineTwoLevelQueryTests {

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
    public void engineShouldIncludeValuesOfAlwaysPermittedInput() {
        Map<String, String> noSelection = Map.of();

        List<String> permittedValues = engine.queryAllowedValues("country", noSelection);

        assertThat(permittedValues).containsExactlyInAnyOrder("United Kingdom", "Hungary");
    }

    @Test
    public void engineShouldIncludeValuesOfSelectedFirstLevelInput() {
        Map<String, String> hungarySelected = Map.of("country", "Hungary");

        List<String> permittedValues = engine.queryAllowedValues("city", hungarySelected);

        assertThat(permittedValues).containsExactlyInAnyOrder("Budapest", "Sopron");
    }

    @Test
    public void engineShouldNotIncludeSecondLevelValuesIfInvalidReceivedInFirstLevelInput() {
        Map<String, String> invalidCountryReceived = Map.of("country", "France");

        List<String> permittedValues = engine.queryAllowedValues("city", invalidCountryReceived);

        assertThat(permittedValues).isEmpty();
    }

}
