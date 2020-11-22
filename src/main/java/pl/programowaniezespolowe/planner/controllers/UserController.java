package pl.programowaniezespolowe.planner.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.programowaniezespolowe.planner.user.User;
import pl.programowaniezespolowe.planner.user.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @CrossOrigin
    @GetMapping(path = "/user")
    public List<User> getUsers() {
        return userRepository.findAll();
    }


    @CrossOrigin
    @GetMapping(path = "/user/{id}")
    public Optional<User> getUser(@PathVariable String id) {
        int userId = Integer.parseInt(id);
        return userRepository.findById(userId);
    }

    @CrossOrigin
    @PostMapping("/user")
    public List<User> createUser(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String surname = body.get("surname");
        int groupid =  Integer.valueOf(body.get("groupid"));
        String email = body.get("email");
        String password = body.get("password");
        String permission = body.get("permission");
        userRepository.save(new User(name, surname, groupid, email, password, permission));
        return userRepository.findAll();
    }

    @CrossOrigin
    @DeleteMapping("user/{id}")
    public List<User> deleteUser(@PathVariable String id) {
        int userId = Integer.parseInt(id);
        userRepository.deleteById(userId);
        return userRepository.findAll();
    }


}
