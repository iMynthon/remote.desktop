package org.mynthon.dto;

public record RemoteServerResponse(String host, int port, String name, Boolean online) {
}
