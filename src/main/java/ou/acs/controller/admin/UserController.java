package ou.acs.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ou.acs.entity.AccessObject;
import ou.acs.entity.User;
import ou.acs.entity.dao.UserDAO;
import ou.acs.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDAO userDAO) {
        return new ResponseEntity<>(userService.addUser(new User(userDAO)), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/objects")
    public ResponseEntity<List<AccessObject>> findObjectsUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findAccessObjectsByUserId(id), HttpStatus.OK);
    }
}
