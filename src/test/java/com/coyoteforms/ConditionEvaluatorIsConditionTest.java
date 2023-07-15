package com.coyoteforms;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ConditionEvaluatorIsConditionTest {

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

    private static ConditionEvaluator conditionEvaluator;

    @BeforeAll
    public static void init() {
        conditionEvaluator = new ConditionEvaluator();
    }

    @Test
    public void evaluatorShouldIncludeValueIfOneLevelConditionIsSatisfiedByInput() {
        Map<String, String> sameCountrySelected = Map.of("country", "Hungary");

        boolean shouldBeIncluded = conditionEvaluator.shouldBeIncluded("country is 'Hungary'", sameCountrySelected);

        assertThat(shouldBeIncluded).isTrue();
    }

    @Test
    public void evaluatorShouldNotIncludeValueIfOneLevelConditionIsNotSatisfiedByInput() {
        Map<String, String> differentCountrySelected = Map.of("country", "United Kingdom");

        boolean shouldBeIncluded = conditionEvaluator.shouldBeIncluded("country is 'Hungary'", differentCountrySelected);

        assertThat(shouldBeIncluded).isFalse();
    }

    @Test
    public void ifInputIdIsNotPresentInInputValuesThenEvaluatorMustNotPermit() {
        Map<String, String> noInput = Map.of();

        boolean shouldBeIncluded = conditionEvaluator.shouldBeIncluded("country is 'Hungary'", noInput);

        assertThat(shouldBeIncluded).isFalse();
    }


}
