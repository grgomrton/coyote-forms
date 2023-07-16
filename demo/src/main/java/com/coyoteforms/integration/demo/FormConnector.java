package com.coyoteforms.integration.demo;

import com.coyoteforms.integration.demo.dto.LocationDto;
import com.coyoteforms.validator.Connector;

import java.util.Map;
import java.util.Optional;

public class FormConnector implements Connector<LocationDto> {

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
