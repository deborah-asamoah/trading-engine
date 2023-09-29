package io.turntabl.project.clientprocessingapi.dtos.requestbodies;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AuthenticateClientRequestBody {
    private String email;
    private String password;
}
