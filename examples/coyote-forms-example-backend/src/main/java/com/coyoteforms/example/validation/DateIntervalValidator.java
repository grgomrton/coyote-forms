package com.coyoteforms.example.validation;

import com.coyoteforms.example.dto.DateIntervalDto;
import com.coyoteforms.validator.CoyoteFormsValidator;
import com.coyoteforms.validator.ValidationFailure;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class DateIntervalValidator implements ConstraintValidator<ValidInterval, DateIntervalDto> {

    private CoyoteFormsValidator<DateIntervalDto> validator;

    @Autowired
    public DateIntervalValidator(CoyoteFormsValidator<DateIntervalDto> validator) {
        this.validator = validator;
    }

    @Override
    public boolean isValid(DateIntervalDto interval, ConstraintValidatorContext context) {
        List<ValidationFailure> validationFailures = validator.validate(interval);

        context.disableDefaultConstraintViolation();
        validationFailures.forEach(failure ->
                context.buildConstraintViolationWithTemplate(getHelperTextOrDefault(failure))
                        .addPropertyNode(failure.getInputId())
                        .addConstraintViolation());
        return validationFailures.isEmpty();
    }

    private String getHelperTextOrDefault(ValidationFailure failure) {
        return failure.getHelperText() != null ? failure.getHelperText() : "Invalid value";
    }

}
