package com.coyoteforms.validator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// I hate utility classes, but now there is too many common code in asserts
// that I really don't want to duplicate
public class TestUtilities {

    public static Set<String> collectInputIds(List<ValidationFailure> validationFailures) {
        return validationFailures.stream().map(ValidationFailure::getInputId).collect(Collectors.toSet());
    }

    // this is a List because I want to catch cases when the same helper text is returned multiple times
    public static List<String> collectHelperTexts(String inputId, List<ValidationFailure> validationFailures) {
        return validationFailures.stream()
                .filter(failure -> inputId.equals(failure.getInputId()))
                .map(ValidationFailure::getHelperText)
                .collect(Collectors.toList());
    }

}
