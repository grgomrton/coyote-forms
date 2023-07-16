package com.coyoteforms.integration.demo.api.forms;

import com.coyoteforms.integration.demo.api.forms.dto.LocationDto;
import com.coyoteforms.integration.demo.api.forms.jakarta.ValidLocation;
import com.coyoteforms.integration.demo.api.forms.validator.LocationFormValidatorProvider;
import com.coyoteforms.validator.CoyoteFormValidator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("api/forms/location")
public class LocationFormController {

    private CoyoteFormValidator<LocationDto> validator;

    public LocationFormController() {
        validator = LocationFormValidatorProvider.get();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{inputId}/allowedValues")
    // here the LocationDto is not validated, so we can acquire input values for fields in partially filled form state
    public List<String> queryAllowedValues(@PathParam("inputId") String inputId, LocationDto inputValues) {
        return validator.queryAllowedValues(inputId, inputValues);
    }

    @POST
    @Path("/save")
    public Response saveLocation(@ValidLocation LocationDto selectedLocation) {
        return Response.status(Response.Status.CREATED).build(); // send created if passed validation
    }

}
