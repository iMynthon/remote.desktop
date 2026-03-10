package org.mynthon.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mynthon.dto.RegRequest;
import org.mynthon.dto.RemoteClientResponse;
import org.mynthon.dto.TokenResponse;
import org.mynthon.exception.EntityNotFoundException;
import org.mynthon.model.RemoteClient;
import org.mynthon.repository.ClientServerRepository;
import org.mynthon.security.SecurityService;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ClientService {

    @Inject
    private ClientServerRepository repository;

    @Inject
    private SecurityService securityService;

    public TokenResponse save(RegRequest regRequest) {
        RemoteClient remoteClient = repository.saveEntity(requestToEntity(regRequest));
        return securityService.saveToken(remoteClient.getNameClient(),remoteClient.getConnectionId());
    }

    public RemoteClientResponse name(String name) {
        RemoteClient client = repository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Don't find client with name"));
        return entityToResponse(client);
    }

    public RemoteClientResponse findById(UUID id) {
        RemoteClient client = repository.findByUUIDID(id).orElseThrow(() -> new EntityNotFoundException("Don't find client with id"));
        return entityToResponse(client);
    }

    public Boolean existsClient(String name, String password) {
        return repository.existsByName(name, password);
    }

    public Boolean existsConnectionId(Integer connectionId) {
        return repository.existsByConnectionId(connectionId);
    }


    private RemoteClient requestToEntity(RegRequest regRequest) {
        return RemoteClient.builder()
                .nameClient(regRequest.login())
                .password(regRequest.password())
                .build();
    }

    private RemoteClientResponse entityToResponse(RemoteClient remoteClient) {
        return RemoteClientResponse.builder()
                .name(remoteClient.getNameClient())
                .connectionId(remoteClient.getConnectionId())
                .build();
    }

}
