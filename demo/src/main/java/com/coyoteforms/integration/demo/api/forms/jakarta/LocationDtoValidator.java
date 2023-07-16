package com.coyoteforms.integration.demo.api.forms.jakarta;

import com.coyoteforms.integration.demo.api.forms.validator.LocationFormValidatorProvider;
import com.coyoteforms.integration.demo.api.forms.dto.LocationDto;
import com.coyoteforms.validator.CoyoteFormValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LocationDtoValidator implements ConstraintValidator<ValidLocation, LocationDto> {

    private CoyoteFormValidator<LocationDto> validator;

    @Override
    public void initialize(ValidLocation constraintAnnotation) {
        validator = LocationFormValidatorProvider.get();
    }

    @Override
    public boolean isValid(LocationDto locationDto, ConstraintValidatorContext constraintValidatorContext) {
        return validator.validate(locationDto).isEmpty(); // we lose useful information: the invalid input ids
    }

}
