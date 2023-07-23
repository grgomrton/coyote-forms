package com.coyoteforms.validator;

import java.util.*;
import java.util.stream.Collectors;

// note: the engine code is something we need to implement in javascript too,
// it is important to stick with language constructs that exist both in java and javascript.
class Engine {

    private List<Rule> constraints;
    private ConditionEvaluator conditionEvaluator;

    Engine(List<Rule> constraints) {
        this.constraints = constraints;
        this.conditionEvaluator = new ConditionEvaluator();
    }

    List<String> queryValidValues(String inputId, Map<String, String> inputValues) {
        return constraints.stream()
                .filter(rule -> Optional.ofNullable(rule.getInputIds()).orElseGet(List::of).contains(inputId))
                .filter(rule -> allConditionMatches(rule, inputValues))
                .flatMap(rule -> rule.getPermittedValues().stream())
                .collect(Collectors.toList());
    }

    private boolean allConditionMatches(Rule rule, Map<String, String> inputValues) {
        return rule.getCondition()
                .stream()
                .map(expression -> conditionEvaluator.shouldBeIncluded(expression, inputValues))
                .reduce(true, (prev, cur) -> prev && cur);
    }

    List<ValidationFailure> validateInput(Map<String, String> inputValues) {
        return inputValues.entrySet()
                .stream()
                .filter(inputEntry ->
                        !queryValidValues(inputEntry.getKey(), inputValues).stream()
                                .anyMatch(item -> inputEntry.getValue().matches(item)))
                .map(Map.Entry::getKey)
                .flatMap(inputId -> collectHelperTexts(inputId).stream()
                        .map(helperText -> ValidationFailure.builder()
                                .inputId(inputId)
                                .helperText(helperText)
                                .build()))
                .collect(Collectors.toList());
    }

    private Set<String> collectHelperTexts(String inputId) {
        return constraints.stream()
                .filter(rule -> Optional.ofNullable(rule.getInputIds()).orElseGet(List::of).contains(inputId))
                .map(Rule::getHelperText)
                .collect(Collectors.toSet());
    }

}
