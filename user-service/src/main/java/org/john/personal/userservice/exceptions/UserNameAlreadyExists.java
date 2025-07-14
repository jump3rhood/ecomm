package org.john.personal.userservice.exceptions;

public class UserNameAlreadyExists extends RuntimeException {
    public UserNameAlreadyExists(String message) {
        super(message);
    }
}
