package pl.rafiki.LibraryProject.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/library/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public void addUser(@RequestBody User user) {
        userService.addUser(user);
    }

    @DeleteMapping(path = "{userId}")
    public void deleteUserById(@PathVariable("userId") Long id) {
        userService.deleteUser(id);
    }

    @PutMapping(path = "{userId}")
    public void updateUser(
            @PathVariable("userId") Long id,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email) {

        userService.updateUser(id, firstName, lastName, email);
    }
}
