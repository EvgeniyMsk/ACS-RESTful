package ou.acs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ou.acs.entity.AccessObject;
import ou.acs.entity.User;
import ou.acs.exception.NotFoundException;
import ou.acs.repository.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User temp = userRepository.findByUsername(username);
        if (temp == null)
            throw new UsernameNotFoundException("User not found");
        return temp;
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll().stream().sorted().collect(Collectors.toList());
    }

    public void updateUserLastDate(String username) {
        User temp = userRepository.findByUsername(username);
        temp.setLastDate(new Date());
        userRepository.save(temp);
    }

    public List<AccessObject> findAccessObjectsByUserId(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> new ArrayList<>(value.getObjects())).orElse(null);
    }
}
