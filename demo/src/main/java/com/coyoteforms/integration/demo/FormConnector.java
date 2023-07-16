package com.coyoteforms.integration.demo;

import com.coyoteforms.integration.demo.dto.LocationDto;
import com.coyoteforms.validator.Connector;

import java.util.Map;

public class FormConnector implements Connector<LocationDto> {
    @Override
    public Map<String, String> collectInputValues(LocationDto obj) {
        return Map.of();
    }
}
