package com.coyoteforms.validator;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class EngineCustomInputTest {

    private static RuleSet onlyRelationshipRules = RuleSet.builder()
            .constraints(
                    List.of(
                            Rule.builder()
                                    .inputId("sumOfAnglesIs180")
                                    .condition(List.of("always"))
                                    .permittedValues(List.of("true", "false"))
                                    .build(),
                            Rule.builder()
                                    .inputId("angle1")
                                    .condition(List.of("sumOfAnglesIs180 is 'true'"))
                                    .permittedValues(List.of(".*"))
                                    .build(),
                            Rule.builder()
                                    .inputId("angle2")
                                    .condition(List.of("sumOfAnglesIs180 is 'true'"))
                                    .permittedValues(List.of(".*"))
                                    .build(),
                            Rule.builder()
                                    .inputId("angle3")
                                    .condition(List.of("sumOfAnglesIs180 is 'true'"))
                                    .permittedValues(List.of(".*"))
                                    .build()
                    )
            ).build();

    private static RuleSet relationshipAndIndividualConstraintsRuleSet = RuleSet.builder()
            .constraints(
                    List.of(
                            Rule.builder()
                                    .inputId("sumOfAnglesIs180")
                                    .condition(List.of("always"))
                                    .permittedValues(List.of("true", "false"))
                                    .build(),
                            Rule.builder()
                                    .inputId("angle1IsPositive")
                                    .condition(List.of("always"))
                                    .permittedValues(List.of("true", "false"))
                                    .build(),
                            Rule.builder()
                                    .inputId("angle2IsPositive")
                                    .condition(List.of("always"))
                                    .permittedValues(List.of("true", "false"))
                                    .build(),
                            Rule.builder()
                                    .inputId("angle3IsPositive")
                                    .condition(List.of("always"))
                                    .permittedValues(List.of("true", "false"))
                                    .build(),
                            Rule.builder()
                                    .inputId("angle1")
                                    .condition(List.of("angle1IsPositive is 'true'", "sumOfAnglesIs180 is 'true'"))
                                    .permittedValues(List.of(".*"))
                                    .build(),
                            Rule.builder()
                                    .inputId("angle2")
                                    .condition(List.of("angle2IsPositive is 'true'", "sumOfAnglesIs180 is 'true'"))
                                    .permittedValues(List.of(".*"))
                                    .build(),
                            Rule.builder()
                                    .inputId("angle3")
                                    .condition(List.of("angle3IsPositive is 'true'", "sumOfAnglesIs180 is 'true'"))
                                    .permittedValues(List.of(".*"))
                                    .build()
                    )
            ).build();

    @Test
    public void allInputShouldBeInvalidIfSumOfAnglesIsFalse() {
        Engine engine = new Engine(onlyRelationshipRules.getConstraints());
        Map<String, String> inputValues = Map.of("sumOfAnglesIs180", "false", "angle1", "60", "angle2", "30", "angle3", "50");
        List<String> invalidInputIds = engine.validateInput(inputValues);
        assertThat(invalidInputIds).containsExactlyInAnyOrder("angle1", "angle2", "angle3");
    }

    @Test
    public void allInputShouldBeValidIfSumOfAnglesIsTrue() {
        Engine engine = new Engine(onlyRelationshipRules.getConstraints());
        Map<String, String> inputValues = Map.of("sumOfAnglesIs180", "true", "angle1", "60", "angle2", "30", "angle3", "90");
        List<String> invalidInputIds = engine.validateInput(inputValues);
        assertThat(invalidInputIds).isEmpty();
    }

    @Test
    public void validationCanBeHackedWithoutIndividualValidators() {
        Engine engine = new Engine(onlyRelationshipRules.getConstraints());
        Map<String, String> inputValues = Map.of("sumOfAnglesIs180", "true", "angle1", "130", "angle2", "60", "angle3", "-10");
        List<String> invalidInputIds = engine.validateInput(inputValues);
        assertThat(invalidInputIds).isEmpty();
    }

    @Test
    public void engineShouldCatchInvalidFieldIfIndividualValidatorsArePresent() {
        Engine engine = new Engine(relationshipAndIndividualConstraintsRuleSet.getConstraints());
        Map<String, String> inputValues = Map.of(
                "sumOfAnglesIs180", "true",
                "angle1", "130", "angle2", "60", "angle3", "-10",
                "angle1IsPositive", "true", "angle2IsPositive", "true", "angle3IsPositive", "false");
        List<String> invalidInputIds = engine.validateInput(inputValues);
        assertThat(invalidInputIds).containsExactlyInAnyOrder("angle3");
    }

    @Test
    public void ifRelevantCustomInputIsMissingValidationShouldNotPass() {
        Engine engine = new Engine(relationshipAndIndividualConstraintsRuleSet.getConstraints());
        Map<String, String> inputValues = Map.of(
                //"sumOfAnglesIs180", "true",
                "angle1", "30", "angle2", "60", "angle3", "90",
                "angle1IsPositive", "true", "angle2IsPositive", "true", "angle3IsPositive", "true");
        List<String> invalidInputIds = engine.validateInput(inputValues);
        assertThat(invalidInputIds).containsExactlyInAnyOrder("angle1", "angle2", "angle3");;
    }

}
