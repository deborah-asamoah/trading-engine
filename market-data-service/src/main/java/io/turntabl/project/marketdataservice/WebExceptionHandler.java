package io.turntabl.project.marketdataservice;

import io.turntabl.project.marketdataservice.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ExceptionResponse> handleEmptyBodyException(Exception ex, WebRequest request) {
        int status = HttpStatus.BAD_REQUEST.value();
        return ResponseEntity.status(status).body(new ExceptionResponse(
                status,
                "Body is required",
                "Bad request"
        ));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ExceptionResponse> handleServerException(Exception ex, WebRequest request) {
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        return ResponseEntity.status(status).body(new ExceptionResponse(
                status,
                ex.getMessage(),
                "Internal server error"
        ));
    }
}
