package io.turntabl.project.clientservice.exceptions;

public class PortfolioDoesNotExistException extends RuntimeException {
    public PortfolioDoesNotExistException(String message) {
        super(message);
    }
}
