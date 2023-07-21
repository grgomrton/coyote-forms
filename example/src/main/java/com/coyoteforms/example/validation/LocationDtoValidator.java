package com.coyoteforms.example.validation;

import com.coyoteforms.example.dto.LocationDto;
import com.coyoteforms.example.dto.TriangleDto;
import com.coyoteforms.validator.CoyoteFormValidator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LocationDtoValidator implements ConstraintValidator<ValidLocation, LocationDto> {

    private CoyoteFormValidator<LocationDto> validator;

    @Autowired
    public LocationDtoValidator(CoyoteFormValidator<LocationDto> validator) {
        this.validator = validator;
    }

    @Override
    public boolean isValid(LocationDto location, ConstraintValidatorContext context) {
        Map<String, Set<String>> validationResult = validator.validate(location);
        context.disableDefaultConstraintViolation();
        validationResult.keySet().forEach(invalidInput -> addHelperTextOrDefaultValue(invalidInput, validationResult, context));
        return validationResult.isEmpty();
    }

    private void addHelperTextOrDefaultValue(String invalidInputId, Map<String, Set<String>> validationResult, ConstraintValidatorContext context) {
        Set<String> helperTexts = validationResult.get(invalidInputId);
        Set<String> responseTexts = helperTexts.isEmpty() ? Set.of("Invalid value") : helperTexts;
        responseTexts.forEach(helperText -> context.buildConstraintViolationWithTemplate(helperText)
                .addPropertyNode(invalidInputId)
                .addConstraintViolation());
    }

}
