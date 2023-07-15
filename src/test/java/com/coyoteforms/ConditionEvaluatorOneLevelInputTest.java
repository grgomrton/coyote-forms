package com.coyoteforms;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class ConditionEvaluatorOneLevelInputTest {

    //Planned input:
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

    @Test
    public void malformedConditionShouldThrow() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> conditionEvaluator.shouldBeIncluded("Hello", Map.of()));
    }

}
