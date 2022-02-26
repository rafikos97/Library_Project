package pl.rafiki.LibraryProject.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.rafiki.LibraryProject.user.exceptions.EmailAlreadyTakenException;
import pl.rafiki.LibraryProject.user.exceptions.InvalidEmailException;
import pl.rafiki.LibraryProject.user.exceptions.UserDoesNotExistsException;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    private UserService underTest;
    @Mock
    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository, emailValidator);
    }

    @Test
    void canGetAllUsers() {
        // when
        underTest.getUsers();

        // then
        verify(userRepository).findAll();
    }

    @Test
    void canAddUser() {
        // given
        User user = new User(
                "Rafał",
                "Nowak",
                "rafiki@gmail.com",
                LocalDate.of(1990, 3, 10)
        );

        given(emailValidator.isEmailValid(user.getEmail())).willReturn(true);

        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());
        given(userOptional).willReturn(Optional.empty());

        // when
        underTest.addUser(user);

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(user);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        // given
        User user = new User(
                "Rafał",
                "Nowak",
                "r.nowak@gmail.com",
                LocalDate.of(1990, 3, 10)
        );

        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());
        given(userOptional).willReturn(Optional.of(user));

        // when
        // then
        assertThatThrownBy(() -> underTest.addUser(user))
                .isInstanceOf(EmailAlreadyTakenException.class)
                .hasMessageContaining("Email address already taken!");

        verify(userRepository, never()).save(any());
    }

    @Test
    void willThrowWhenEmailIsInvalid() {
        // given
        User user = new User(
                "Rafał",
                "Nowak",
                "r.nowak",
                LocalDate.of(1990, 3, 10)
        );

        given(emailValidator.isEmailValid(user.getEmail())).willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> underTest.addUser(user))
                .isInstanceOf(InvalidEmailException.class)
                .hasMessageContaining("Email is invalid!");

        verify(userRepository, never()).save(any());
    }

    @Test
    void canDeleteUser() {
        //given
        Long id = 1L;

        given(userRepository.existsById(id)).willReturn(true);

        // when
        underTest.deleteUser(id);

        // then
        verify(userRepository).deleteById(id);
    }

    @Test
    void willThrowWhenUserDoesNotExist() {
        //given
        Long id = 1L;

        given(userRepository.existsById(id)).willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> underTest.deleteUser(id))
                .isInstanceOf(UserDoesNotExistsException.class)
                .hasMessageContaining("User with id " + id + " does not exists!");

        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void canUpdateUser() {
        // given
        User user = new User(
                1L,
                "Rafał",
                "Nowak",
                "rafiki@gmail.com",
                LocalDate.of(1990, 3, 10),
                21
        );

        String nameToUpdate = "Andrzej";
        String lastNameToUpdate = "Kowalski";
        String emailToUpdate = "a.kowalski@gmail.com";

        Optional<User> userOptional = userRepository.findById(user.getId());
        given(userOptional).willReturn(Optional.of(user));

        given(emailValidator.isEmailValid(emailToUpdate)).willReturn(true);

        Optional<User> userOptionalEmail = userRepository.findUserByEmail(emailToUpdate);
        given(userOptionalEmail).willReturn(Optional.empty());

        // when
        underTest.updateUser(1L, nameToUpdate, lastNameToUpdate, emailToUpdate);

        // then
        assertThat(user.getFirstname()).isEqualTo(nameToUpdate);

        assertThat(user.getLastname()).isEqualTo(lastNameToUpdate);

        assertThat(user.getEmail()).isEqualTo(emailToUpdate);
    }

    @Test
    void willThrowWhenEmailIsTakenDuringUpdate() {
        // given
        User user = new User(
                1L,
                "Rafał",
                "Nowak",
                "rafiki@gmail.com",
                LocalDate.of(1990, 3, 10),
                21
        );

        User user2 = new User(
                2L,
                "Antoni",
                "Kowalski",
                "a.kowalski@gmail.com",
                LocalDate.of(1990, 3, 10),
                21
        );

        String nameToUpdate = "Andrzej";
        String lastNameToUpdate = "Kowalski";
        String emailToUpdate = "a.kowalski@gmail.com";

        Optional<User> userOptional = userRepository.findById(user.getId());
        given(userOptional).willReturn(Optional.of(user));

        given(emailValidator.isEmailValid(emailToUpdate)).willReturn(true);

        Optional<User> userOptional2 = userRepository.findUserByEmail(emailToUpdate);
        given(userOptional2).willReturn(Optional.of(user2));


        // when
        // then
        assertThatThrownBy(() -> underTest.updateUser(1L, nameToUpdate, lastNameToUpdate, emailToUpdate))
                .isInstanceOf(EmailAlreadyTakenException.class)
                .hasMessageContaining("Email address already taken!");
    }
}