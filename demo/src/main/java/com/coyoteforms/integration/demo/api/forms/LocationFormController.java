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

    /**
     * Assist the front-end with list of allowed values for a particular input field.
     * Here the LocationDto is not validated, therefore input values can be acquired for fields in a
     * partially filled form state.
     *
     * @param inputId
     * @param inputValues
     * @return
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{inputId}/allowedValues")
    public List<String> queryAllowedValues(@PathParam("inputId") String inputId, LocationDto inputValues) {
        return validator.queryAllowedValues(inputId, inputValues);
    }

    /**
     * Save a selected location. Here, the dto is validated, and 400 is returned on validation failure,
     * while 201 is returned on successful validation.
     * @param selectedLocation
     * @return
     */
    @POST
    @Path("/save")
    public Response saveLocation(@ValidLocation LocationDto selectedLocation) {
        return Response.status(Response.Status.CREATED).build(); // send created if passed validation
    }

}
