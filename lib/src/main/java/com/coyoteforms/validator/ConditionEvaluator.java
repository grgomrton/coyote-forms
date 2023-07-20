package com.coyoteforms.validator;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ConditionEvaluator {

    private static Pattern IS_CONDITION_PATTERN = Pattern.compile("^(?<inputId>[a-zA-Z][a-zA-Z0-9]*) is (?<expectedInputValue>.*)$");
    private static Pattern ALWAYS_PATTERN = Pattern.compile("always");

    boolean shouldBeIncluded(String condition, Map<String, String> inputValues) {
        Matcher alwaysMatcher = ALWAYS_PATTERN.matcher(condition);
        if (alwaysMatcher.matches()) {
            return true;
        }
        Matcher isMatcher = IS_CONDITION_PATTERN.matcher(condition);
        if (isMatcher.matches()) {
            String inputId = isMatcher.group("inputId");
            String expectedInputValue = isMatcher.group("expectedInputValue");
            return expectedInputValue.equals(inputValues.get(inputId));
        }
        throw new IllegalArgumentException("Not supported condition");
    }

}
