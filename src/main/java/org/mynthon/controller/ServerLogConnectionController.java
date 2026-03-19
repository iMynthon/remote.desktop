package org.mynthon.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.mynthon.dto.ServerLogConnectionResponse;
import org.mynthon.service.ServerService;

import java.util.UUID;

@Path("/server/log/connection")
public class ServerLogConnectionController {

    @Inject
    private ServerService serverService;

    @GET
    @Path("/{clientId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ServerLogConnectionResponse getConnection(@PathParam("clientId") UUID clientId){
        return serverService.getLogConnectionClient(clientId);
    }
}
