package com.coyoteforms;

import java.util.Map;

public class ConditionEvaluator {

    public boolean shouldBeIncluded(String condition, Map<String, String> inputValues) {
        if (condition.equals("always")) {
            return true;
        } else if (condition.contains(" is ")) { // this is very naive, will fail on first 'ez is az is' parameter
            String[] keyValuePair = condition.split(" is ");
            String inputId = keyValuePair[0];
            String expectedInputValue = keyValuePair[1].substring(1, keyValuePair[1].length() - 1);
            return expectedInputValue.equals(inputValues.get(inputId));
        }
        return false;
    }

}
