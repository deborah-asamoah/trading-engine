package io.turntabl.project.clientprocessingapi.dtos;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PortfolioDTO {
    private UUID id;
    private String name;
    private boolean defaultPortfolio;
    private UUID clientID;
}
