package org.mynthon.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.mynthon.dto.ClientRequest;
import org.mynthon.dto.RemoteClientResponse;
import org.mynthon.service.ClientService;

import java.util.UUID;

@Path("remote/client")
public class RemoteClientController {

    @Inject
    private ClientService service;

    @GET
    @Path("/{name}")
    public RemoteClientResponse getClientName(@PathParam("name") String name){
        return service.name(name);
    }

    @GET
    @Path("/{id}")
    public RemoteClientResponse getClientId(@PathParam("id")UUID id){
        return service.findById(id);
    }

    @POST
    @Path("/public/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RemoteClientResponse postSave(ClientRequest request){
        return service.save(request);
    }
}
