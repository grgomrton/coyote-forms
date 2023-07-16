package com.coyoteforms.integration.demo.api.forms.validator;

import com.coyoteforms.integration.demo.api.forms.dto.LocationDto;
import com.coyoteforms.validator.CoyoteFormValidator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *  Build and provide Coyote Forms validator validating the LocationDto.
 */
public class LocationFormValidatorProvider {

    private static final String LOCATION_DTO_VALIDATOR_RULE_SET = "LocationFormRuleSet.json";
    private static CoyoteFormValidator<LocationDto> LOCATION_DTO_VALIDATOR_INSTANCE;

    public static CoyoteFormValidator<LocationDto> get() {
        synchronized (LOCATION_DTO_VALIDATOR_RULE_SET) {
            if (LOCATION_DTO_VALIDATOR_INSTANCE == null) {
                String ruleSet = loadRuleSet();
                LOCATION_DTO_VALIDATOR_INSTANCE = new CoyoteFormValidator<>(ruleSet, new Connector());
            }
        }
        return LOCATION_DTO_VALIDATOR_INSTANCE;
    }

    private static String loadRuleSet() {
        try {
            return Files.readString(Paths.get(ClassLoader.getSystemResource(LOCATION_DTO_VALIDATOR_RULE_SET).toURI()));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
