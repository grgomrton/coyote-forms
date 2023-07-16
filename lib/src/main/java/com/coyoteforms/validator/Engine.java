package com.coyoteforms.validator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// note: the engine code is something we need to implement in javascript too,
// it is important to stick with language tools that exists in both Java and javascript.
class Engine {

    private List<Rule> rules;
    private ConditionEvaluator conditionEvaluator;

    Engine(List<Rule> rules) {
        this.rules = rules;
        this.conditionEvaluator = new ConditionEvaluator();
    }

    List<String> queryAllowedValues(String inputId, Map<String, String> inputValues) {
        return rules.stream()
                .filter(rule -> inputId.equals(rule.getInputId()))
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

    List<String> validateInput(Map<String, String> inputValues) {
        return inputValues.entrySet()
                .stream()
                .filter(entry -> !queryAllowedValues(entry.getKey(), inputValues).contains(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
