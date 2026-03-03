package org.mynthon.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mynthon.dto.RemoteServerResponse;
import org.mynthon.dto.ServerRequest;
import org.mynthon.service.ServerService;

import java.util.UUID;

@Path("/remote/server")
public class RemoteServerController {

    @Inject
    private ServerService serverService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public RemoteServerResponse getServer(@PathParam("id") UUID id) {
        return serverService.findById(id);
    }

    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postGreeting(ServerRequest request) {
        return Response.ok(serverService.create(request)).build();
    }
}
