package com.coyoteforms.validator;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class EngineCustomInputTest {

    // Planned input
    // {
    //	"discreteValueRules": [
    //		{
    //			"inputId": "sumOfArcsIs180",
    //			"allowedValues": [ "true", "false" ]
    //		}
    //	],
    //	"passThroughRules": [
    //		{
    //			"inputId": "arc1",
    //			"condition": "sumOfArcsIs180 is 'true'"
    //		},
    //		{
    //			"inputId": "arc2",
    //			"condition": "sumOfArcsIs180 is 'true'"
    //		},
    //		{
    //			"inputId": "arc3",
    //			"condition": "sumOfArcsIs180 is 'true'"
    //		}
    //	]
    // }

    private static RuleSet onlyRelationshipRules = RuleSet.builder()
            .discreteValueRules(
                    List.of(
                            DiscreteRule.builder()
                                    .inputId("sumOfArcsIs180")
                                    .condition(List.of("always"))
                                    .permittedValues(List.of("true", "false"))
                                    .build(),
                            DiscreteRule.builder()
                                    .inputId("arc1")
                                    .condition(List.of("sumOfArcsIs180 is 'true'"))
                                    .permittedValues(List.of(".*"))
                                    .build(),
                            DiscreteRule.builder()
                                    .inputId("arc2")
                                    .condition(List.of("sumOfArcsIs180 is 'true'"))
                                    .permittedValues(List.of(".*"))
                                    .build(),
                            DiscreteRule.builder()
                                    .inputId("arc3")
                                    .condition(List.of("sumOfArcsIs180 is 'true'"))
                                    .permittedValues(List.of(".*"))
                                    .build()
                    )
            ).build();

    private static RuleSet relationshipAndIndividualConstraintsRuleSet = RuleSet.builder()
            .discreteValueRules(
                    List.of(
                            DiscreteRule.builder()
                                    .inputId("sumOfArcsIs180")
                                    .condition(List.of("always"))
                                    .permittedValues(List.of("true", "false"))
                                    .build(),
                            DiscreteRule.builder()
                                    .inputId("arc1IsPositive")
                                    .condition(List.of("always"))
                                    .permittedValues(List.of("true", "false"))
                                    .build(),
                            DiscreteRule.builder()
                                    .inputId("arc2IsPositive")
                                    .condition(List.of("always"))
                                    .permittedValues(List.of("true", "false"))
                                    .build(),
                            DiscreteRule.builder()
                                    .inputId("arc3IsPositive")
                                    .condition(List.of("always"))
                                    .permittedValues(List.of("true", "false"))
                                    .build(),
                            DiscreteRule.builder()
                                    .inputId("arc1")
                                    .condition(List.of("arc1IsPositive is 'true'", "sumOfArcsIs180 is 'true'"))
                                    .permittedValues(List.of(".*"))
                                    .build(),
                            DiscreteRule.builder()
                                    .inputId("arc2")
                                    .condition(List.of("arc2IsPositive is 'true'", "sumOfArcsIs180 is 'true'"))
                                    .permittedValues(List.of(".*"))
                                    .build(),
                            DiscreteRule.builder()
                                    .inputId("arc3")
                                    .condition(List.of("arc3IsPositive is 'true'", "sumOfArcsIs180 is 'true'"))
                                    .permittedValues(List.of(".*"))
                                    .build()
                    )
            ).build();

    @Test
    public void allInputShouldBeInvalidIfSumOfArcsIsFalse() {
        Engine engine = new Engine(onlyRelationshipRules);
        Map<String, String> inputValues = Map.of("sumOfArcsIs180", "false", "arc1", "60", "arc2", "30", "arc3", "50");
        List<String> invalidInputIds = engine.validateInput(inputValues);
        assertThat(invalidInputIds).containsExactlyInAnyOrder("arc1", "arc2", "arc3");
    }

    @Test
    public void allInputShouldBeValidIfSumOfArcsIsTrue() {
        Engine engine = new Engine(onlyRelationshipRules);
        Map<String, String> inputValues = Map.of("sumOfArcsIs180", "true", "arc1", "60", "arc2", "30", "arc3", "90");
        List<String> invalidInputIds = engine.validateInput(inputValues);
        assertThat(invalidInputIds).isEmpty();
    }

    @Test
    public void validationCanBeHackedWithoutIndividualValidators() {
        Engine engine = new Engine(onlyRelationshipRules);
        Map<String, String> inputValues = Map.of("sumOfArcsIs180", "true", "arc1", "130", "arc2", "60", "arc3", "-10");
        List<String> invalidInputIds = engine.validateInput(inputValues);
        assertThat(invalidInputIds).isEmpty();
    }

    @Test
    public void engineShouldCatchInvalidFieldIfIndividualValidatorsArePresent() {
        Engine engine = new Engine(relationshipAndIndividualConstraintsRuleSet);
        Map<String, String> inputValues = Map.of(
                "sumOfArcsIs180", "true",
                "arc1", "130", "arc2", "60", "arc3", "-10",
                "arc1IsPositive", "true", "arc2IsPositive", "true", "arc3IsPositive", "false");
        List<String> invalidInputIds = engine.validateInput(inputValues);
        assertThat(invalidInputIds).containsExactlyInAnyOrder("arc3");
    }

    @Test
    public void ifRelevantCustomInputIsMissingValidationShouldNotPass() {
        Engine engine = new Engine(relationshipAndIndividualConstraintsRuleSet);
        Map<String, String> inputValues = Map.of(
                //"sumOfArcsIs180", "true",
                "arc1", "30", "arc2", "60", "arc3", "90",
                "arc1IsPositive", "true", "arc2IsPositive", "true", "arc3IsPositive", "true");
        List<String> invalidInputIds = engine.validateInput(inputValues);
        assertThat(invalidInputIds).containsExactlyInAnyOrder("arc1", "arc2", "arc3");;
    }

}
