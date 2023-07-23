package com.coyoteforms.validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class ConditionEvaluatorIsConditionTest {

    private static ConditionEvaluator conditionEvaluator;

    @BeforeAll
    public static void init() {
        conditionEvaluator = new ConditionEvaluator();
    }

    @Test
    public void evaluatorShouldIncludeValueIfOneLevelConditionIsSatisfiedByInput() {
        Map<String, String> sameCountrySelected = Map.of("country", "Hungary");

        boolean shouldBeIncluded = conditionEvaluator.shouldBeIncluded("country is Hungary", sameCountrySelected);

        assertThat(shouldBeIncluded).isTrue();
    }

    @Test
    public void evaluatorShouldNotIncludeValueIfOneLevelConditionIsNotSatisfiedByInput() {
        Map<String, String> differentCountrySelected = Map.of("country", "United Kingdom");

        boolean shouldBeIncluded = conditionEvaluator.shouldBeIncluded("country is Hungary", differentCountrySelected);

        assertThat(shouldBeIncluded).isFalse();
    }

    @Test
    public void ifInputIdIsNotPresentInInputValuesThenEvaluatorMustNotPermit() {
        Map<String, String> noInput = Map.of();

        boolean shouldBeIncluded = conditionEvaluator.shouldBeIncluded("country is Hungary", noInput);

        assertThat(shouldBeIncluded).isFalse();
    }

    @Test
    public void evaluatorShouldPermitWordIsInConditionRegex() {
        Map<String, String> inputWithIs = Map.of("text", "ez is az is");
        boolean shouldBeIncluded = conditionEvaluator.shouldBeIncluded("text is ez is az is", inputWithIs);
        assertThat(shouldBeIncluded).isTrue();
    }

    @Test
    public void conditionWithoutExpectedValueShouldThrow() {
        assertThatIllegalArgumentException().isThrownBy(() -> conditionEvaluator.shouldBeIncluded("a is ", Map.of()));
    }

    @Test
    public void conditionWithoutInputIdShouldThrow() {
        assertThatIllegalArgumentException().isThrownBy(() -> conditionEvaluator.shouldBeIncluded(" is c", Map.of()));
    }

}
