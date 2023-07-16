package com.coyoteforms.integration.demo.api.forms.jakarta;

import com.coyoteforms.integration.demo.api.forms.validator.ValidatorProvider;
import com.coyoteforms.integration.demo.api.forms.model.Location;
import com.coyoteforms.validator.CoyoteFormValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * This is the custom validator implementation that uses Coyote Forms validator to assert the fields.
 */
public class ValidLocationImpl implements ConstraintValidator<ValidLocation, Location> {

    private CoyoteFormValidator<Location> validator;

    @Override
    public void initialize(ValidLocation constraintAnnotation) {
        validator = ValidatorProvider.get();
    }

    @Override
    public boolean isValid(Location location, ConstraintValidatorContext constraintValidatorContext) {
        return validator.validate(location).isEmpty();
    }

}
