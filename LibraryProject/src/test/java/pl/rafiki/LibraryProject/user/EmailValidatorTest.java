package pl.rafiki.LibraryProject.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {

    EmailValidator emailValidator = new EmailValidator();

    @Test
    void isEmailValid() {
        //given
        String email = "rafiki@gmail.com";

        // when
        // then
        assertTrue(emailValidator.isEmailValid(email));

    }
}