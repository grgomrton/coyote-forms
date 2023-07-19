package com.coyoteforms.validator;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

// note: the engine code is something we need to implement in javascript too,
// it is important to stick with language tools that exists in both Java and javascript.
class Engine {

    private RuleSet rules;
    private ConditionEvaluator conditionEvaluator;

    Engine(RuleSet rules) {
        this.rules = rules;
        this.conditionEvaluator = new ConditionEvaluator();
    }

    List<String> queryAllowedValues(String inputId, Map<String, String> inputValues) {
        return Optional.ofNullable(rules.getDiscreteValueRules()).orElseGet(List::of).stream()
                .filter(rule -> inputId.equals(rule.getInputId()))
                .filter(rule -> allConditionMatches(rule.getCondition(), inputValues))
                .flatMap(rule -> rule.getPermittedValues().stream())
                .collect(Collectors.toList());
    }

    List<String> validateInput(Map<String, String> inputValues) {
        return inputValues.entrySet()
                .stream()
                .filter(inputEntry ->
                        !(queryAllowedValues(inputEntry.getKey(), inputValues).contains(inputEntry.getValue()) ||
                          passesPassThroughValidation(inputEntry.getKey(), inputValues)))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private boolean passesPassThroughValidation(String inputId, Map<String, String> inputValues) {
        List<PassThroughRule> inputConstraints = Optional.ofNullable(rules.getPassThroughRules()).orElseGet(List::of).stream()
                .filter(rule -> inputId.equals(rule.getInputId()))
                .collect(Collectors.toList());
        return inputConstraints.size() > 0 ?
                inputConstraints.stream().anyMatch(rule -> allConditionMatches(rule.getCondition(), inputValues)) :
                true;
    }

    private boolean allConditionMatches(List<String> condition, Map<String, String> inputValues) {
        return condition.stream()
                .map(operand -> conditionEvaluator.shouldBeIncluded(operand, inputValues))
                .reduce(true, (prev, cur) -> prev && cur);
    }

}
