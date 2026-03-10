package org.mynthon.dto;

import lombok.Builder;

@Builder
public record RemoteClientResponse(String name,Integer connectionId) {
}
