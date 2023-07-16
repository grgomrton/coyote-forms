package com.coyoteforms.integration.demo.api.forms.validator;

import com.coyoteforms.integration.demo.api.forms.dto.LocationDto;

import java.util.Map;
import java.util.Optional;

/**
 * This class maps the fields of the dto to the keys and values of the input values Map.
 */
public class Connector implements com.coyoteforms.validator.Connector<LocationDto> {

    @Override
    public Map<String, String> collectInputValues(LocationDto obj) {
        return Map.of(
                "region", getValueOrEmptyStringIfNull(obj.getRegion()),
                "country", getValueOrEmptyStringIfNull(obj.getCountry()),
                "city", getValueOrEmptyStringIfNull(obj.getCity())
        );
    }

    private String getValueOrEmptyStringIfNull(String retrievedValue) {
        return Optional.ofNullable(retrievedValue).orElse("");
    }

}