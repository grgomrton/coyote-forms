package com.coyoteforms.integration.demo.api.forms;

import com.coyoteforms.integration.demo.api.forms.validator.FormConnector;
import com.coyoteforms.integration.demo.api.forms.dto.LocationDto;
import com.coyoteforms.integration.demo.api.forms.validator.ValidLocation;
import com.coyoteforms.validator.CoyoteFormValidator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Path("api/forms/location")
public class LocationFormController {

    private CoyoteFormValidator<LocationDto> validator;

    public LocationFormController() {
        String ruleSet = loadRuleSet();
        validator = new CoyoteFormValidator<>(ruleSet, new FormConnector());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{inputId}/allowedValues")
    public List<String> queryAllowedValues(@PathParam("inputId") String inputId, LocationDto inputValues) {
        return validator.queryAllowedValues(inputId, inputValues);
    }

    @POST
    @Path("/save")
    public Response saveLocation(@ValidLocation LocationDto selectedLocation) {
        // send created if passed validation
        return Response.status(Response.Status.CREATED).build();
    }

    private String loadRuleSet() {
        try {
            return Files.readString(Paths.get(ClassLoader.getSystemResource("LocationFormRuleSet.json").toURI()));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
