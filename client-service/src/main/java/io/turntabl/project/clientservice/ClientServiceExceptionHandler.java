package io.turntabl.project.clientservice;


import io.turntabl.project.clientservice.exceptions.EmailAlreadyExists;
import io.turntabl.project.clientservice.exceptions.ExceptionResponse;
import io.turntabl.project.clientservice.exceptions.PortfolioDoesNotExistException;
import io.turntabl.project.clientservice.exceptions.EmailDoesNotExists;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ClientServiceExceptionHandler {

    @ExceptionHandler(EmailAlreadyExists.class)
    public ResponseEntity<ExceptionResponse> handleEmailAlreadyExistsException(EmailAlreadyExists emailAlreadyExists) {
        return ResponseEntity
                .status(400)
                .body(new ExceptionResponse(
                        400,
                        emailAlreadyExists.getMessage(),
                        "Bad request"
                ));
    }

    @ExceptionHandler(EmailDoesNotExists.class)
    public ResponseEntity<ExceptionResponse> handleEmailDoesNotExistsException(EmailDoesNotExists emailDoesNotExists) {
        return ResponseEntity
                .status(400)
                .body(new ExceptionResponse(
                        400,
                        emailDoesNotExists.getMessage(),
                        "Bad request"
                ));
    }

    @ExceptionHandler(PortfolioDoesNotExistException.class)
    public ResponseEntity<ExceptionResponse> handleEmailAlreadyExistsException(PortfolioDoesNotExistException exception) {
        return ResponseEntity
                .status(404)
                .body(new ExceptionResponse(
                        404,
                        exception.getMessage(),
                        "Portfolio not found"
                ));
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity
                .status(404)
                .body(new ExceptionResponse(
                        404,
                        ex.getMessage(),
                        "Invalid email or password"

                ));
    }
}
