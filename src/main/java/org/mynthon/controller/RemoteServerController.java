package org.mynthon.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mynthon.dto.ServerRequest;
import org.mynthon.service.ServerService;

@Path("/remote/server")
public class RemoteServerController {

    @Inject
    private ServerService serverService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@PathParam("id")  String id) {
        return "Hello from Quarkus REST";
    }

    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postGreeting(ServerRequest request) {
        return Response.ok(serverService.create(request)).build();
    }
}
