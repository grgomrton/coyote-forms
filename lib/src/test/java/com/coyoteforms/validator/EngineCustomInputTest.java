package com.coyoteforms.validator;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.coyoteforms.validator.TestUtilities.collectHelperTexts;
import static com.coyoteforms.validator.TestUtilities.collectInputIds;
import static org.assertj.core.api.Assertions.assertThat;

public class EngineCustomInputTest {

    private static RuleSet ONLY_RELATIONSHIP_RULES = RuleSet.builder()
            .constraints(
                    List.of(
                            Rule.builder()
                                    .inputIds(List.of("sumOfAnglesIs180"))
                                    .condition(List.of("always"))
                                    .permittedValues(List.of("true", "false"))
                                    .build(),
                            Rule.builder()
                                    .inputIds(List.of("angle1"))
                                    .condition(List.of("sumOfAnglesIs180 is true"))
                                    .permittedValues(List.of(".+"))
                                    .helperText("The sum of the angles must be 180")
                                    .build(),
                            Rule.builder()
                                    .inputIds(List.of("angle2"))
                                    .condition(List.of("sumOfAnglesIs180 is true"))
                                    .permittedValues(List.of(".+"))
                                    .helperText("The sum of the angles must be 180")
                                    .build(),
                            Rule.builder()
                                    .inputIds(List.of("angle3"))
                                    .condition(List.of("sumOfAnglesIs180 is true"))
                                    .permittedValues(List.of(".+"))
                                    .helperText("The sum of the angles must be 180")
                                    .build()
                    )
            ).build();

    private static RuleSet RELATIONSHIP_AND_SINGLE_OPERAND_RULE_SET = RuleSet.builder()
            .constraints(
                    List.of(
                            Rule.builder()
                                    .inputIds(List.of("sumOfAnglesIs180"))
                                    .condition(List.of("always"))
                                    .permittedValues(List.of("true", "false"))
                                    .build(),
                            Rule.builder()
                                    .inputIds(List.of("angle1IsPositive"))
                                    .condition(List.of("always"))
                                    .permittedValues(List.of("true", "false"))
                                    .build(),
                            Rule.builder()
                                    .inputIds(List.of("angle2IsPositive"))
                                    .condition(List.of("always"))
                                    .permittedValues(List.of("true", "false"))
                                    .build(),
                            Rule.builder()
                                    .inputIds(List.of("angle3IsPositive"))
                                    .condition(List.of("always"))
                                    .permittedValues(List.of("true", "false"))
                                    .build(),
                            Rule.builder()
                                    .inputIds(List.of("angle1"))
                                    .condition(List.of("angle1IsPositive is true", "sumOfAnglesIs180 is true"))
                                    .permittedValues(List.of(".+"))
                                    .helperText("Angle must be positive and sum of the angles must be 180")
                                    .build(),
                            Rule.builder()
                                    .inputIds(List.of("angle2"))
                                    .condition(List.of("angle2IsPositive is true", "sumOfAnglesIs180 is true"))
                                    .permittedValues(List.of(".+"))
                                    .helperText("Angle must be positive and sum of the angles must be 180")
                                    .build(),
                            Rule.builder()
                                    .inputIds(List.of("angle3"))
                                    .condition(List.of("angle3IsPositive is true", "sumOfAnglesIs180 is true"))
                                    .permittedValues(List.of(".+"))
                                    .helperText("Angle must be positive and sum of the angles must be 180")
                                    .build()
                    )
            ).build();

    @Test
    public void allInputShouldBeInvalidIfSumOfAnglesIsFalse() {
        Engine engine = new Engine(ONLY_RELATIONSHIP_RULES.getConstraints());
        Map<String, String> inputValues = Map.of("sumOfAnglesIs180", "false", "angle1", "60", "angle2", "30", "angle3", "50");

        List<ValidationFailure> validationErrors = engine.validateInput(inputValues);

        assertThat(collectInputIds(validationErrors)).containsExactlyInAnyOrder("angle1", "angle2", "angle3");
        assertThat(collectHelperTexts("angle1", validationErrors))
                .containsExactlyInAnyOrder("The sum of the angles must be 180");
        assertThat(collectInputIds(validationErrors)).doesNotContain("sumOfAnglesIs180");
    }

    @Test
    public void allInputShouldBeValidIfSumOfAnglesIsTrue() {
        Engine engine = new Engine(ONLY_RELATIONSHIP_RULES.getConstraints());
        Map<String, String> inputValues = Map.of("sumOfAnglesIs180", "true", "angle1", "60", "angle2", "30", "angle3", "90");

        List<ValidationFailure> validationErrors = engine.validateInput(inputValues);

        assertThat(collectInputIds(validationErrors)).isEmpty();
    }

    @Test
    public void validationCanBeHackedWithoutIndividualValidators() {
        Engine engine = new Engine(ONLY_RELATIONSHIP_RULES.getConstraints());
        Map<String, String> inputValues = Map.of("sumOfAnglesIs180", "true", "angle1", "130", "angle2", "60", "angle3", "-10");

        List<ValidationFailure> validationErrors = engine.validateInput(inputValues);

        assertThat(collectInputIds(validationErrors)).isEmpty();
    }

    @Test
    public void engineShouldCatchInvalidFieldIfIndividualValidatorsArePresent() {
        Engine engine = new Engine(RELATIONSHIP_AND_SINGLE_OPERAND_RULE_SET.getConstraints());
        Map<String, String> inputValues = Map.of(
                "sumOfAnglesIs180", "true",
                "angle1", "130", "angle2", "60", "angle3", "-10",
                "angle1IsPositive", "true", "angle2IsPositive", "true", "angle3IsPositive", "false");

        List<ValidationFailure> validationErrors = engine.validateInput(inputValues);

        assertThat(collectInputIds(validationErrors)).containsExactlyInAnyOrder("angle3");
        assertThat(collectHelperTexts("angle3", validationErrors))
                .containsExactlyInAnyOrder("Angle must be positive and sum of the angles must be 180");
    }

    @Test
    public void ifRelevantCustomInputIsMissingValidationShouldNotPass() {
        Engine engine = new Engine(RELATIONSHIP_AND_SINGLE_OPERAND_RULE_SET.getConstraints());
        Map<String, String> inputValues = Map.of(
                //"sumOfAnglesIs180", "true",
                "angle1", "30", "angle2", "60", "angle3", "90",
                "angle1IsPositive", "true", "angle2IsPositive", "true", "angle3IsPositive", "true");

        List<ValidationFailure> validationErrors = engine.validateInput(inputValues);

        assertThat(collectInputIds(validationErrors)).containsExactlyInAnyOrder("angle1", "angle2", "angle3");
        assertThat(collectHelperTexts("angle2", validationErrors))
                .containsExactlyInAnyOrder("Angle must be positive and sum of the angles must be 180");
    }

    @Test
    public void forContinuousInputsQueryAllowedValuesShouldNotThrow() {
        Engine engine = new Engine(RELATIONSHIP_AND_SINGLE_OPERAND_RULE_SET.getConstraints());
        Map<String, String> inputValues = Map.of(
                "sumOfAnglesIs180", "true",
                "angle1", "30", "angle2", "60", "angle3", "90",
                "angle1IsPositive", "true", "angle2IsPositive", "true", "angle3IsPositive", "true");

        List<String> allowedValuesAngle1 = engine.queryValidValues("angle1", inputValues);

        assertThat(allowedValuesAngle1).containsExactlyInAnyOrder(".+"); // fine by me :)
    }

}
