package pl.rafiki.LibraryProject.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @Test
    void shouldFindUserByEmail() {
        // given
        String email = "r.nowak@gmail.com";
        User user = new User(
                "Rafał",
                "Nowak",
                email,
                LocalDate.of(1990, 3, 10)
        );

        underTest.save(user);

        // when
        Optional<User> foundUser = underTest.findUserByEmail(email);

        // then
        assertThat(foundUser).isPresent();
    }
}