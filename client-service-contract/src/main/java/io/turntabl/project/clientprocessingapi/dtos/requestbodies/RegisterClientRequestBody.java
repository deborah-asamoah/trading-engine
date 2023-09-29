package io.turntabl.project.clientprocessingapi.dtos.requestbodies;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RegisterClientRequestBody {
    private String name;
    private String email;
    private String password;
}
