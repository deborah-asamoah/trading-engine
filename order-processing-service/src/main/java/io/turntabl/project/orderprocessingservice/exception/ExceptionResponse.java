package io.turntabl.project.orderprocessingservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponse {
    private int statusCode;
    private String message;
    private String error;
}
