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
        this.engine = new Engine(deserializedRuleSet.getRules());
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
        Map<String, String> inputKeyValues = connector.collectInputValues(input);
        return engine.validateInput(inputKeyValues);
    }

}
