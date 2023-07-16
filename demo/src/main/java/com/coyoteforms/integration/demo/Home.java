package com.coyoteforms.integration.demo;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Path("/")
public class Home {

    private static String HOME_FILENAME = "index.html";

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getHome() throws URISyntaxException, IOException {
        return Response
                .status(Response.Status.OK)
                .entity(Files.readString(Paths.get(ClassLoader.getSystemResource(HOME_FILENAME).toURI())))
                .build();
    }

}
