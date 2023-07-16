package com.coyoteforms.integration.demo.api.forms;

import com.coyoteforms.integration.demo.api.forms.model.InvalidInputIdListResponse;
import com.coyoteforms.integration.demo.api.forms.model.Location;
import com.coyoteforms.integration.demo.api.forms.jakarta.ValidLocation;
import com.coyoteforms.integration.demo.api.forms.validator.LocationFormValidatorProvider;
import com.coyoteforms.validator.CoyoteFormValidator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("api/forms/location")
public class LocationFormController {

    private CoyoteFormValidator<Location> validator;

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
    @Path("/input/{inputId}/allowedValues")
    public List<String> queryAllowedValues(@PathParam("inputId") String inputId, Location inputValues) {
        return validator.queryAllowedValues(inputId, inputValues);
    }

    /**
     * Form support, validate the input values and return the invalid input ids.
     *
     * @param inputValues
     * @return
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/validate")
    public Response validateForm(Location inputValues) {
        List<String> invalidInputIds = validator.validate(inputValues);
        if (!invalidInputIds.isEmpty()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(InvalidInputIdListResponse.builder().invalidInputIds(invalidInputIds).build())
                    .build();
        } else {
            return Response.status(Response.Status.OK).build();
        }
    }

    /**
     * Save a selected location. Here, the dto is validated, and 400 is returned on validation failure,
     * while 201 is returned on successful validation.
     * @param selectedLocation
     *
     * @return
     */
    @POST
    @Path("/save")
    public Response saveLocation(@ValidLocation Location selectedLocation) {
        return Response.status(Response.Status.CREATED).build(); // send created if passed validation
    }

}
