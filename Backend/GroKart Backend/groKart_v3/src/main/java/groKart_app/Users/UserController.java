package groKart_app.Users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    User getUserById( @PathVariable int id){
        return userRepository.findById(id);
    }

    // LOGIN
    @GetMapping(path = "/users/{userName}/{password}")
    String getUserById( @PathVariable String userName, @PathVariable String password) {
        User user = userRepository.findByUserNameAndPassword(userName, password);

        if (user == null) return "Failed login";
        else return "Successful login";
    }

    /**
     * CREATE USER
     * @param user
     * @return
     */
    @PostMapping(path = "/users")
    String createUser(@RequestBody User user){
        if (user == null || userRepository.existsByUserName(user.getUserName()))
            return failure;
        userRepository.save(user);
        return success;
    }

    /**
     * UPDATE USER
     * @param userName
     * @param request
     * @return
     */
    @PutMapping("/users/{userName}")
    User updateUser(@PathVariable String userName, @RequestBody User request){
        User user = userRepository.findByUserName(userName);
        if (user == null)
            return null;
        userRepository.deleteByUserName((userName));
        userRepository.save(request);
        return userRepository.findByUserName(userName);
    }

    /**
     * DELETE USER
     * @param userName
     * @return
     */
    @DeleteMapping(path = "/users/{userName}")
    String deleteUser(@PathVariable String userName){
        userRepository.deleteByUserName(userName);
        return success;
    }
}
