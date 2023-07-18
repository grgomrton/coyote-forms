package com.coyoteforms.example.validation;

import com.coyoteforms.example.dto.LocationDto;
import com.coyoteforms.validator.CoyoteFormValidator;
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
        List<String> invalidInputIds = validator.validate(location);
        context.disableDefaultConstraintViolation();
        invalidInputIds.forEach(inputId -> context.buildConstraintViolationWithTemplate("Invalid value")
                .addPropertyNode(inputId)
                .addConstraintViolation()
        );
        return invalidInputIds.isEmpty();
    }

}
