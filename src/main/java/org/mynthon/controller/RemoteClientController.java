package org.mynthon.controller;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.mynthon.dto.ClientRequest;
import org.mynthon.model.RemoteClient;
import org.mynthon.service.ClientService;

@Path("remote/client")
public class RemoteClientController {

    private ClientService service;

//    @GET
//    @Path("/{id}")
//    public RemoteClient getClient(@PathParam("id") String name){
//        return service.
//    }

    @POST
    @Path("/save")
    public RemoteClient postSave(ClientRequest request){
        return service.save(request);
    }
}
