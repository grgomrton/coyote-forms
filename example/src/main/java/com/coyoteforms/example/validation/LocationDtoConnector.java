package com.coyoteforms.example.validation;

import com.coyoteforms.example.dto.LocationDto;
import com.coyoteforms.validator.Connector;

import java.util.Map;

public class LocationDtoConnector implements Connector<LocationDto> {

    @Override
    public Map<String, String> collectInputValues(LocationDto obj) {
        return Map.of(
                "region", obj.getRegion() == null ? "" : obj.getRegion(),
                "country", obj.getCountry() == null ? "" : obj.getCountry(),
                "city", obj.getCity() == null ? "" : obj.getCity()
        );
    }

}
