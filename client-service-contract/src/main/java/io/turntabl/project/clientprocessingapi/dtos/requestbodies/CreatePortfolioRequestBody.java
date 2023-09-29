package io.turntabl.project.clientprocessingapi.dtos.requestbodies;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class CreatePortfolioRequestBody {
    private String name;
    private UUID clientID;
}
