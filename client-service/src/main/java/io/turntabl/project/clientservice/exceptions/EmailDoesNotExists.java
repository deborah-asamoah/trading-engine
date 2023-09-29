package io.turntabl.project.clientservice.exceptions;

public class EmailDoesNotExists extends RuntimeException {
    public EmailDoesNotExists(){
        super("Sorry, this email does not exists");
    }
}
