package org.mynthon.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mynthon.service.ClientService;

@ApplicationScoped
public class SecurityApproval {

    @Inject
    private ClientService clientService;

    public boolean checkAuthorizationDB(String name){
        return clientService.existsClient(name);
    }
}
