package com.coyoteforms.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class CoyoteFormValidator<T> {

    private Engine engine;

    private Connector<T> connector;

    public CoyoteFormValidator(String ruleSet, Connector<T> connector) {
        RuleSet deserializedRuleSet = parseRuleSet(ruleSet);
        this.engine = new Engine(deserializedRuleSet);
        this.connector = connector;
    }

    private RuleSet parseRuleSet(String ruleSet) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(ruleSet, RuleSet.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> validate(T input) {
        Map<String, String> inputKeyValuePairs = connector.collectInputValues(input);
        return engine.validateInput(inputKeyValuePairs);
    }

    public List<String> queryAllowedValues(String inputId, T inputValues) {
        Map<String, String> inputKeyValuePairs = connector.collectInputValues(inputValues);
        return engine.queryAllowedValues(inputId, inputKeyValuePairs);
    }

}
