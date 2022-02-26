package pl.rafiki.LibraryProject.user.exceptions;

public class EmailAlreadyTakenException extends RuntimeException {

    public EmailAlreadyTakenException(String message) {
        super(message);
    }
}
