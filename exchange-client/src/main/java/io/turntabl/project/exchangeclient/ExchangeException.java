package io.turntabl.project.exchangeclient;

import io.turntabl.project.exchangeclient.dtos.responsebodies.ExceptionResponseBody;
import jakarta.ws.rs.WebApplicationException;
import lombok.Getter;

import java.io.IOException;

@Getter
public class ExchangeException extends RuntimeException {
    private final ExceptionResponseBody details;

    public ExchangeException(String message, ExceptionResponseBody details, WebApplicationException cause) throws IOException {
        super(message, cause);
        this.details = details;
    }
}
