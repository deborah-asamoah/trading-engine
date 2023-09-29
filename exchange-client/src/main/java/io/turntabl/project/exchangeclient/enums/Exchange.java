package io.turntabl.project.exchangeclient.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.URI;

@AllArgsConstructor
@Getter
public enum Exchange {
    MAL1(URI.create("https://exchange.matraining.com")), MAL2(URI.create("https://exchange2.matraining.com"));

    private final URI url;
}
