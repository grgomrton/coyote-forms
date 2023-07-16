package com.coyoteforms.integration.demo;

import com.coyoteforms.integration.demo.dto.LocationDto;
import com.coyoteforms.validator.CoyoteFormValidator;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("api/validation")
public class ValidationEndpoints {

    private CoyoteFormValidator<LocationDto> validator;

    public ValidationEndpoints() {
        String ruleSet = loadRuleSet();
        validator = new CoyoteFormValidator<>(ruleSet, new FormConnector());
    }

    private String loadRuleSet() {
        try {
            return Files.readString(Paths.get(ClassLoader.getSystemResource("RuleSet.json").toURI()));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getIt() {
        return "Got it!";
    }
}
