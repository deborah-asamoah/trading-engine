package io.turntabl.project.clientprocessingapi.dtos.responsebodies;


import io.turntabl.project.clientprocessingapi.dtos.ClientDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListClientsResponseBody {
    private List<ClientDTO> clientDTOS;
}
