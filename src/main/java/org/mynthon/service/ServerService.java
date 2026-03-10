package org.mynthon.service;

import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ServerService {

    private final Map<Integer, WebSocketConnection> connectionsMap = new ConcurrentHashMap<>();

    public void saveToMapAndDB(Integer connectionId, WebSocketConnection connection) {
        connectionsMap.put(connectionId,connection);
    }

    public byte[] desktopRemoteScreen(){
        try {
            Robot robot = new Robot();
            Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage image = robot.createScreenCapture(rectangle);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image,"jpeg",baos);
            return baos.toByteArray();
        } catch (AWTException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
