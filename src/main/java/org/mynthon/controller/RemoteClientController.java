package org.mynthon.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.mynthon.dto.RemoteClientResponse;
import org.mynthon.service.ClientService;

import java.util.UUID;

@Path("/remote/client")
public class RemoteClientController {

    @Inject
    private ClientService service;

    @GET
    @Path("/findId/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RemoteClientResponse getClientId(@PathParam("id") String id){
        return service.findById(UUID.fromString(id));
    }

    @GET
    @Path("/findName/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public RemoteClientResponse getClientName(@PathParam("name") String name){
        return service.name(name);
    }
}
