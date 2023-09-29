package io.turntabl.project.orderprocessingservice;

import io.turntabl.project.exchangeclient.ExchangeException;
import io.turntabl.project.orderprocessingservice.exception.ExceptionResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class OrderExceptionHandler {

    @ExceptionHandler({ExchangeException.class})
    public ResponseEntity<ExceptionResponse> handleExchangeException(Exception ex, WebRequest request) {
        int status = HttpStatus.BAD_REQUEST.value();
        return ResponseEntity.status(status).body(new ExceptionResponse(
                status,
                ex.getMessage(),
                "Bad request"
        ));
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleMissingEntityException(Exception ex, WebRequest request) {
        int status = HttpStatus.NOT_FOUND.value();
        return ResponseEntity.status(status).body(new ExceptionResponse(
                status,
                ex.getMessage(),
                "Not found"
        ));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ExceptionResponse> handleServiceException(Exception ex, WebRequest request) {
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        return ResponseEntity.status(status).body(new ExceptionResponse(
                status,
                ex.getMessage(),
                "Internal server error"
        ));
    }
}
