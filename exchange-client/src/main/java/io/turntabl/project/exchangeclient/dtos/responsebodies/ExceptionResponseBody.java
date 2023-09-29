package io.turntabl.project.exchangeclient.dtos.responsebodies;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class ExceptionResponseBody {
    private OffsetDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
