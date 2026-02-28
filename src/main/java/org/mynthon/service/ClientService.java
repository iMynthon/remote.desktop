package org.mynthon.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mynthon.dto.ClientRequest;
import org.mynthon.model.RemoteClient;
import org.mynthon.repository.ClientServerRepository;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ClientService {

    private ClientServerRepository repository;

    public RemoteClient save(ClientRequest request){
        RemoteClient client = repository.saveEntity(requestToEntity(request));
        return client;
    }

    private RemoteClient requestToEntity(ClientRequest request){
        return RemoteClient.builder()
                .nameClient(request.name())
                .namePc(request.namePc())
                .build();
    }
}
