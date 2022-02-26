package pl.rafiki.LibraryProject.user;

import org.springframework.stereotype.Component;

@Component
public class EmailValidator {
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

    public boolean isEmailValid(String email) {
        return email.matches(EMAIL_PATTERN);
    }
}
