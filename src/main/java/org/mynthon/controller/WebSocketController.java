package org.mynthon.controller;

import io.quarkus.websockets.next.*;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.Cancellable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.mynthon.service.ClientService;
import org.mynthon.service.ServerService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ApplicationScoped
@WebSocket(path = "/remote-control/{connectionId}")
public class WebSocketController {

    private final Map<Integer, WebSocketConnection> connectionsMap = new ConcurrentHashMap<>();

    @Inject
    private ClientService clientService;

    @Inject
    private ServerService service;

    @OnOpen
    @Blocking
    public Uni<Void> onOpen(WebSocketConnection webSocketConnection, @PathParam("connectionId") String connectionId) {
        Integer connectId = Integer.parseInt(connectionId);
        log.info("WebSocket opened for client: {}", connectionId);
        if(!clientService.existsConnectionId(connectId)){
            log.info("Connection id not found: {}",connectId);
            return webSocketConnection
                    .close(new CloseReason(Response.Status.UNAUTHORIZED.getStatusCode(),
                            Response.Status.UNAUTHORIZED.toString()));
        }
        service.saveToMapAndDB(connectId,webSocketConnection);
        return webSocketConnection.sendText("Connected to remote control server (next-gen)");
    }

    @OnBinaryMessage
    public Cancellable onScreenshot(WebSocketConnection connection, String message, @PathParam("connectionId") String connectionId){
        log.info("WebSocket get screen desktop: {} - client: {}",message, connectionId);
        return connection.sendBinary(service.desktopRemoteScreen())
                .subscribe().with(
                        success -> log.debug("Screenshot sent to {}", connectionId),
                        failure -> log.error("Failed to send screenshot to {}", connectionId, failure)
                );
    }

    @OnTextMessage
    public Uni<Void> connectionGetScreen(WebSocketConnection connection, String message, @PathParam("connectionId") String connectionId){
        log.info("Received from {}: {}", connectionId, message);
        onScreenshot(connection,message,connectionId);
        return connection.sendText("Screenshot requested");
    }

    @OnClose
    public void onClose(WebSocketConnection connection,@PathParam("connectionId") String connectionId){
        log.info("WebSocket close connection: {}",connectionId);
        connectionsMap.remove(Integer.parseInt(connectionId));
    }
}
