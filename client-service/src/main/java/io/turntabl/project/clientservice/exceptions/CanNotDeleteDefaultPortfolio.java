package io.turntabl.project.clientservice.exceptions;

public class CanNotDeleteDefaultPortfolio extends RuntimeException{
    public CanNotDeleteDefaultPortfolio() {
        super("Sorry, you cannot delete a default portfolio");
    }
}
