package org.mynthon.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mynthon.dto.RemoteServerResponse;
import org.mynthon.dto.ServerRequest;
import org.mynthon.model.RemoteServer;
import org.mynthon.repository.RemoteServerRepository;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ServerService {

    @Inject
    private RemoteServerRepository serverRepository;

    public RemoteServerResponse create(ServerRequest request) {
       RemoteServer server = serverRepository.saveEntity(requestToEntity(request));
       return entityToResponse(server);
    }

    private RemoteServer requestToEntity(ServerRequest request){
        return RemoteServer.builder()
                .name(request.name())
                .host("192.168.0.82")
                .port(request.port())
                .online(true)
                .build();
    }

    private RemoteServerResponse entityToResponse(RemoteServer server){
        return new RemoteServerResponse(server.getHost(),
                server.getPort(),server.getName(),server.getOnline());
    }
}
