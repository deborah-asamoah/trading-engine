package io.turntabl.project.exchangeclient;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.net.URI;

@Builder
@Getter
public class ExchangeClientConfigParams {
    @NonNull
    private URI baseUrl;
    @NonNull
    private String apiKey;
    private Integer maxTotalConnections;
    private Integer defaultMaxPerRoute;
}
