package com.coyoteforms.integration.demo.api.forms.validator;

import com.coyoteforms.integration.demo.api.forms.dto.LocationDto;
import com.coyoteforms.validator.CoyoteFormValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LocationDtoValidator implements ConstraintValidator<ValidLocation, LocationDto> {

    private CoyoteFormValidator<LocationDto> validator;

    @Override
    public void initialize(ValidLocation constraintAnnotation) {
        String ruleSet = loadRuleSet();
        validator = new CoyoteFormValidator<>(ruleSet, new FormConnector());
    }

    @Override
    public boolean isValid(LocationDto locationDto, ConstraintValidatorContext constraintValidatorContext) {
        return validator.validate(locationDto).isEmpty(); // we lose useful information, the invalid input ids
    }

    private String loadRuleSet() {
        try {
            return Files.readString(Paths.get(ClassLoader.getSystemResource("LocationFormRuleSet.json").toURI()));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
