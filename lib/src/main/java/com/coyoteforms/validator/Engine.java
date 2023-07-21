package com.coyoteforms.validator;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

// note: the engine code is something we need to implement in javascript too,
// it is important to stick with language tools that exists in both Java and javascript.
class Engine {

    private List<Rule> constraints;
    private ConditionEvaluator conditionEvaluator;

    Engine(List<Rule> constraints) {
        this.constraints = constraints;
        this.conditionEvaluator = new ConditionEvaluator();
    }

    List<String> queryAllowedValues(String inputId, Map<String, String> inputValues) {
        return constraints.stream()
                .filter(rule -> Optional.ofNullable(rule.getInputIds()).orElseGet(List::of).contains(inputId))
                .filter(rule -> allConditionMatches(rule, inputValues))
                .flatMap(rule -> rule.getPermittedValues().stream())
                .collect(Collectors.toList());
    }

    private boolean allConditionMatches(Rule rule, Map<String, String> inputValues) {
        return rule.getCondition()
                .stream()
                .map(operand -> conditionEvaluator.shouldBeIncluded(operand, inputValues))
                .reduce(true, (prev, cur) -> prev && cur);
    }

    Map<String, Set<String>> validateInput(Map<String, String> inputValues) {
        return inputValues.entrySet()
                .stream()
                .filter(inputEntry ->
                        !queryAllowedValues(inputEntry.getKey(), inputValues).stream()
                                .anyMatch(item -> inputEntry.getValue().matches(item)))
                .map(Map.Entry::getKey)
                .collect(Collectors.toMap(Function.identity(), this::collectHelperTexts));
    }

    private Set<String> collectHelperTexts(String inputId) {
        return constraints.stream()
                .filter(rule -> Optional.ofNullable(rule.getInputIds()).orElseGet(List::of).contains(inputId))
                .map(Rule::getHelperText)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

}
