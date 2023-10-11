package io.turntabl.project.clientservice.exceptions;

public class NameCannotBeBlank extends Exception{
    public NameCannotBeBlank() {
        super("Name cannot be blank");
    }
}
