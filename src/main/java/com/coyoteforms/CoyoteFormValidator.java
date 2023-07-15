package com.coyoteforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CoyoteFormValidator<T> {

    private Engine engine;

    private Connector<T> connector;

    public CoyoteFormValidator(String ruleSet, Connector<T> connector) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        RuleSet deserializedRules = mapper.readValue(ruleSet, RuleSet.class);
        this.engine = new Engine(deserializedRules.rules);
        this.connector = connector;
    }

}
