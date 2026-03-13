package org.mynthon.service;

import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mynthon.model.RemoteClient;
import org.mynthon.model.ServerLogConnection;
import org.mynthon.repository.ServerRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

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

}
