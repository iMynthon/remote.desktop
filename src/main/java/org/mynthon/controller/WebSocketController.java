package org.mynthon.controller;

import io.quarkus.websockets.next.*;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.mynthon.service.ClientService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ApplicationScoped
@WebSocket(path = "/remote-control/{connectionId}")
public class WebSocketController {

    private final Map<Integer, WebSocketConnection> connectionsMap = new ConcurrentHashMap<>();

    @Inject
    private ClientService clientService;

    @OnOpen
    @Blocking
    public Uni<Void> onOpen(WebSocketConnection webSocketConnection, @PathParam("connectionId") String connectionId) {
        Integer connectId = Integer.parseInt(connectionId);
        log.info("WebSocket opened for client: {}", connectionId);
        if(!clientService.existsConnectionId(connectId)){
            return webSocketConnection
                    .close(new CloseReason(Response.Status.UNAUTHORIZED.getStatusCode(),
                            Response.Status.UNAUTHORIZED.toString()));
        }
        connectionsMap.put(connectId, webSocketConnection);
        return webSocketConnection.sendText("Connected to remote control server (next-gen)");
    }

    @OnTextMessage
    public Uni<Void> onMessage(WebSocketConnection connection,String message,@PathParam("connectionId") String connectionId){
        log.info("WebSocket send message: {} - client: {}",message, connectionId);
        return connection.sendText("Hello I'am Jhon");
    }

    @OnClose
    public void onClose(WebSocketConnection connection,@PathParam("connectionId") String connectionId){
        log.info("WebSocket close connection: {}",connectionId);
        connectionsMap.remove(Integer.parseInt(connectionId));
    }
}
