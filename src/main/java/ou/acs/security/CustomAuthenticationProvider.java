package ou.acs.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ou.acs.entity.User;
import ou.acs.service.UserService;

@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomAuthenticationProvider(UserService userService) {
        this.userService = userService;
        this.passwordEncoder = NoOpPasswordEncoder.getInstance();
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userService.findByUsername(username);
        if (user != null &&
                user.getUsername().equals(username) &&
                passwordEncoder.matches(password, user.getPassword()))
        {
            return new UsernamePasswordAuthenticationToken(
                    userService.loadUserByUsername(username),
                    userService.loadUserByUsername(username).getPassword(),
                    userService.loadUserByUsername(username).getAuthorities()
            );
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}