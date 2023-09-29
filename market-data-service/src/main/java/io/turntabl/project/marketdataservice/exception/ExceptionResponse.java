package io.turntabl.project.marketdataservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponse {
    private int statusCode;
    private String message;
    private String error;
}
