package com.coyoteforms;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ConditionEvaluatorOneLevelInputTest {

    //Imaginary input:
    //
    //  [
    //  {
    //    "inputId": "country",
    //    "condition": "always",
    //    "permittedValues": ["United Kingdom", "Hungary"]
    //  }
    //  ]

    private static ConditionEvaluator conditionEvaluator;

    @BeforeAll
    public static void init() {
        conditionEvaluator = new ConditionEvaluator();
    }

    @Test
    public void evaluatorShouldIncludeValueIfConditionIsAlways() {
        Map<String, String> singleInputValue = Map.ofEntries(Map.entry("country", "United Kingdom"));

        boolean shouldBeIncluded = conditionEvaluator.shouldBeIncluded("always", singleInputValue);

        assertThat(shouldBeIncluded).isTrue();
    }

}
