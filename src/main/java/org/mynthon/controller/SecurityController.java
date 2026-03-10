package org.mynthon.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.mynthon.dto.RegRequest;
import org.mynthon.dto.TokenResponse;
import org.mynthon.service.ClientService;

@Path("/api/v1/register/client")
public class SecurityController {

    @Inject
    private ClientService clientService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TokenResponse postReg(RegRequest regRequest){
       return clientService.save(regRequest);
    }
}
