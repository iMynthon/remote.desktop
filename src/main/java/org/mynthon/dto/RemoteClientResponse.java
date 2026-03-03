package org.mynthon.dto;

import lombok.Builder;

@Builder
public record RemoteClientResponse(String name,String namePc,RemoteServerResponse serverResponse) {
}
