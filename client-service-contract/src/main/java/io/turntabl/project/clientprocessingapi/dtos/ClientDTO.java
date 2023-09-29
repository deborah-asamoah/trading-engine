package io.turntabl.project.clientprocessingapi.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class ClientDTO {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String token;
    private String role;
}
