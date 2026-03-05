package org.mynthon.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mynthon.dto.ClientRequest;
import org.mynthon.dto.RemoteClientResponse;
import org.mynthon.dto.RemoteServerResponse;
import org.mynthon.exception.EntityNotFoundException;
import org.mynthon.model.RemoteClient;
import org.mynthon.repository.ClientServerRepository;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ClientService {

    @Inject
    private ClientServerRepository repository;

    public RemoteClientResponse save(ClientRequest request){
        RemoteClient client = repository.saveEntity(requestToEntity(request));
        return entityToResponse(client);
    }

    public RemoteClientResponse name(String name){
        RemoteClient client = repository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Don't find client with name"));
        return entityToResponse(client);
    }

    public RemoteClientResponse findById(UUID id){
        RemoteClient client = repository.findByUUIDID(id).orElseThrow(() -> new EntityNotFoundException("Don't find client with id"));
        return entityToResponse(client);
    }

    public Boolean existsClient(String name, String password){
        return repository.existsByName(name,password);
    }


    private RemoteClient requestToEntity(ClientRequest request){
        return RemoteClient.builder()
                .nameClient(request.name())
                .password(request.password())
                .namePc(request.namePc())
                .build();
    }

    private RemoteClientResponse entityToResponse(RemoteClient remoteClient){
        return RemoteClientResponse.builder()
                .name(remoteClient.getNameClient())
                .namePc(remoteClient.getNamePc())
                .serverResponse(checkNullableServer(remoteClient))
                .build();
    }

    private RemoteServerResponse checkNullableServer(RemoteClient remoteClient){
        if(!Objects.isNull(remoteClient.getRemoteServer())) {
            return new RemoteServerResponse(
                    remoteClient.getRemoteServer().getHost(),
                    remoteClient.getRemoteServer().getPort(),
                    remoteClient.getRemoteServer().getName(),
                    remoteClient.getRemoteServer().getOnline());
        }
        return new RemoteServerResponse("0.0.0.0",0,"not name", false);
    }
}
