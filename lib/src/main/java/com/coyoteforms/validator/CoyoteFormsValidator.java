package com.coyoteforms.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class CoyoteFormsValidator<T> {

    private Engine engine;

    private Connector<T> connector;

    public CoyoteFormsValidator(String ruleSet, Connector<T> connector) {
        RuleSet deserializedRuleSet = parseRuleSet(ruleSet);
        this.engine = new Engine(deserializedRuleSet.getConstraints());
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

    public List<ValidationFailure> validate(T inputValues) {
        Map<String, String> inputKeyValuePairs = connector.collectInputValues(inputValues);
        return engine.validateInput(inputKeyValuePairs);
    }

    public List<String> queryValidValueSet(String inputId, T inputValues) {
        Map<String, String> inputKeyValuePairs = connector.collectInputValues(inputValues);
        return engine.queryValidValueSet(inputId, inputKeyValuePairs);
    }

}
