package com.coyoteforms.validator;

import java.util.List;

class RuleSet {

    private List<Rule> rules;

    List<Rule> getRules() {
        return rules;
    }

    void setRules(List<Rule> rules) {
        this.rules = rules;
    }
}
