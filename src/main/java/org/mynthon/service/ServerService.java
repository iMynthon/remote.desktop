package org.mynthon.service;

import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mynthon.dto.ServerLogConnectionResponse;
import org.mynthon.model.RemoteClient;
import org.mynthon.model.ServerLogConnection;
import org.mynthon.repository.ServerRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ServerService {

    private final Map<Integer, WebSocketConnection> connectionsMap = new ConcurrentHashMap<>();

    @Inject
    private ClientService clientService;
    @Inject
    private ServerRepository serverRepository;

    @Transactional
    public void saveToMapAndDB(Integer connectionId, WebSocketConnection connection) {
        log.info("Callable method saveToMapAndDB: param connectionId - websocketConnection: {} - {}",connectionId,connection.id());
        connectionsMap.put(connectionId, connection);
        RemoteClient remoteClient = clientService.getRCByConnectionId(connectionId);
        CompletableFuture.runAsync(() -> {
            log.info("Client connection save log to database");
            serverRepository.saveLogConnection(ServerLogConnection.builder()
                    .clientId(remoteClient.getId())
                    .connectionId(connectionId)
                    .build());
        }).exceptionally(fn -> {
            log.info(fn.getMessage());
            return null;
        });
    }

    public ServerLogConnectionResponse getLogConnectionClient(UUID clientId){
        log.info("Calling method getLogConnectionClient: param - clientId: {}",clientId);
        List<ServerLogConnection> logConnectionList = serverRepository.findLogConnectionByClientId(clientId);
        return entityToResponse(clientId,logConnectionList);
    }


    public byte[] desktopRemoteScreen() {
        try {
            log.info("Callable method desktopRemoteScreen: no param");
            Robot robot = new Robot();
            Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage image = robot.createScreenCapture(rectangle);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", baos);
            log.info("Return byteArray");
            return baos.toByteArray();
        } catch (AWTException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ServerLogConnectionResponse entityToResponse(UUID clientId,List<ServerLogConnection> logConnectionList){
       List<ServerLogConnectionResponse.LogConnection> logConnections = logConnectionList.stream()
               .map(lc -> new ServerLogConnectionResponse.LogConnection(lc.getConnectionId(),lc.getConnectionCreateTime().toString()))
               .toList();
       return new ServerLogConnectionResponse(clientId,logConnections);
    }

}
