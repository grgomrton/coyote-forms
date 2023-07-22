package com.coyoteforms.example.validation;

import com.coyoteforms.example.dto.LocationDto;
import com.coyoteforms.validator.CoyoteFormValidator;
import com.coyoteforms.validator.ValidationFailure;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class LocationDtoValidator implements ConstraintValidator<ValidLocation, LocationDto> {

    private CoyoteFormValidator<LocationDto> validator;

    @Autowired
    public LocationDtoValidator(CoyoteFormValidator<LocationDto> validator) {
        this.validator = validator;
    }


    @Override
    public boolean isValid(LocationDto location, ConstraintValidatorContext context) {
        List<ValidationFailure> validationFailures = validator.validate(location);

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
