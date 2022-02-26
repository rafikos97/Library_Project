package pl.rafiki.LibraryProject.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rafiki.LibraryProject.user.exceptions.EmailAlreadyTakenException;
import pl.rafiki.LibraryProject.user.exceptions.InvalidEmailException;
import pl.rafiki.LibraryProject.user.exceptions.UserDoesNotExistsException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final EmailValidator emailValidator;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository, EmailValidator emailValidator) {
        this.userRepository = userRepository;
        this.emailValidator = emailValidator;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());

        if (userOptional.isPresent()) {
            throw new EmailAlreadyTakenException("Email address already taken!");
        }

        if (!emailValidator.isEmailValid(user.getEmail())) {
            throw new InvalidEmailException("Email is invalid!");
        }

        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        boolean exists = userRepository.existsById(id);
        if (!exists) {
            throw new UserDoesNotExistsException("User with id " + id + " does not exists!");
        }

        userRepository.deleteById(id);
    }

    @Transactional
    public void updateUser(Long id, String firstName, String lastName, String email) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserDoesNotExistsException("User with id " + id + " does not exists!"));


        if (firstName != null && !Objects.equals(user.getFirstname(), firstName)) {
            user.setFirstname(firstName);
        }

        if (lastName != null && !Objects.equals(user.getLastname(), lastName)) {
            user.setLastname(lastName);
        }

        if (email != null && emailValidator.isEmailValid(email) && !Objects.equals(user.getEmail(), email)) {

            Optional<User> userOptional = userRepository.findUserByEmail(email);

            if (userOptional.isPresent()) {
                throw new EmailAlreadyTakenException("Email address already taken!");
            }

            user.setEmail(email);
        }
    }
}
