package com.coyoteforms.example.validation;

import com.coyoteforms.example.dto.TriangleDto;
import com.coyoteforms.validator.CoyoteFormsValidator;
import com.coyoteforms.validator.ValidationFailure;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class TriangleDtoValidator implements ConstraintValidator<ValidTriangle, TriangleDto> {

    private CoyoteFormsValidator<TriangleDto> validator;

    @Autowired
    public TriangleDtoValidator(CoyoteFormsValidator<TriangleDto> validator) {
        this.validator = validator;
    }

    @Override
    public boolean isValid(TriangleDto triangle, ConstraintValidatorContext context) {
        List<ValidationFailure> validationFailures = validator.validate(triangle);

        context.disableDefaultConstraintViolation();
        validationFailures.forEach(failure ->
                context.buildConstraintViolationWithTemplate(getHelperTextOrDefault(failure))
                    .addPropertyNode(failure.getInputId()) // we implicitly expect input id to match dto field id, which is bad. maybe a Map to resolve these?
                    .addConstraintViolation());
        return validationFailures.isEmpty();
    }

    private String getHelperTextOrDefault(ValidationFailure failure) {
        return failure.getHelperText() != null ? failure.getHelperText() : "Invalid value";
    }

}
