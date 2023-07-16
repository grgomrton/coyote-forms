package com.coyoteforms.integration.demo;

import com.coyoteforms.integration.demo.dto.LocationDto;
import com.coyoteforms.validator.CoyoteFormValidator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Path("api/validation/location-form")
public class LocationValidationController {

    private CoyoteFormValidator<LocationDto> validator;

    public LocationValidationController() {
        String ruleSet = loadRuleSet();
        validator = new CoyoteFormValidator<>(ruleSet, new FormConnector());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{inputId}/allowedValues")
    public List<String> queryAllowedValues(@PathParam("inputId") String inputId, LocationDto inputValues) {
        return validator.queryAllowedValues(inputId, inputValues);
    }

    private String loadRuleSet() {
        try {
            return Files.readString(Paths.get(ClassLoader.getSystemResource("LocationFormRuleSet.json").toURI()));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
