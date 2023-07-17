package com.coyoteforms.example.validation;

import com.coyoteforms.example.dto.LocationDto;
import com.coyoteforms.validator.CoyoteFormValidator;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public void initialize(ValidLocation constraintAnnotation) { }

    @Override
    public boolean isValid(LocationDto location, ConstraintValidatorContext context) {
        List<String> invalidInputIds = validator.validate(location);
        HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
        invalidInputIds.forEach(inputId -> hibernateContext.addMessageParameter("inputId", inputId));
        return invalidInputIds.isEmpty();
    }

}
