package io.turntabl.project.clientservice.mappers;

import io.turntabl.project.clientprocessingapi.dtos.ClientDTO;
import io.turntabl.project.clientprocessingapi.dtos.requestbodies.AuthenticateClientRequestBody;
import io.turntabl.project.clientprocessingapi.dtos.requestbodies.RegisterClientRequestBody;

import io.turntabl.project.persistence.entities.Client;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ClientRequestBodyMapper {
    Client toClient(RegisterClientRequestBody createClientRequestBody);

    Client toClient(AuthenticateClientRequestBody authenticateClientRequestBody);

    ClientDTO toClientDTO(Client client);

    Client toClient(ClientDTO clientDTO);

}
