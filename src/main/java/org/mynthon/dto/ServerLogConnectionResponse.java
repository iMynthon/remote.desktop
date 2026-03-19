package org.mynthon.dto;

import java.util.List;
import java.util.UUID;

public record ServerLogConnectionResponse(UUID clientId, List<LogConnection> logConnectionList) {
    public record LogConnection(Integer connectionId, String connectionTime){}
}
