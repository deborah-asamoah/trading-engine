package io.turntabl.project.clientprocessingapi.dtos.responsebodies;


import io.turntabl.project.clientprocessingapi.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterClientResponseBody {
    private UUID id;
    private String name;
    private String email;
    private String accessToken;
    private Role role;
}
