package io.turntabl.project.clientservice.exceptions;

public class EmailAlreadyExists extends RuntimeException{
    public EmailAlreadyExists() {
        super("Sorry,this email is already in use");
    }
}
