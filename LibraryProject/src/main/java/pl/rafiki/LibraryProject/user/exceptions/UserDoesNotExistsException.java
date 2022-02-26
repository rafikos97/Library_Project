package pl.rafiki.LibraryProject.user.exceptions;

public class UserDoesNotExistsException extends RuntimeException {

    public UserDoesNotExistsException(String message) {
        super(message);
    }
}
