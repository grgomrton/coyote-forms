package com.coyoteforms.example.validation;

import com.coyoteforms.example.dto.LocationDto;
import com.coyoteforms.example.dto.TriangleDto;
import com.coyoteforms.validator.CoyoteFormValidator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.List;

public class TriangleDtoValidator implements ConstraintValidator<ValidTriangle, TriangleDto> {

    private CoyoteFormValidator<TriangleDto> validator;

    @Autowired
    public TriangleDtoValidator(CoyoteFormValidator<TriangleDto> validator) {
        this.validator = validator;
    }

    @Override
    public boolean isValid(TriangleDto triangle, ConstraintValidatorContext context) {
        List<String> invalidInputIds = validator.validate(triangle);
        context.disableDefaultConstraintViolation();
        invalidInputIds.forEach(inputId -> context.buildConstraintViolationWithTemplate("Invalid value")
                .addPropertyNode(inputId)
                .addConstraintViolation()
        );
        return invalidInputIds.isEmpty();    }
}
