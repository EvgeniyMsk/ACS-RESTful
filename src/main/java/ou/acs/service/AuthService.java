package ou.acs.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ou.acs.requests.LoginRequest;
import ou.acs.responses.LoginResponse;
import ou.acs.security.JWTTokenProvider;

@Service
@Slf4j
public class AuthService {
    private final JWTTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Autowired
    public AuthService(JWTTokenProvider jwtTokenProvider,
                       AuthenticationManager authenticationManager,
                       UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    public ResponseEntity<LoginResponse> authenticateRequest(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUserName(),
                loginRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        userService.updateUserLastDate(loginRequest.getUserName());
        log.info(String.format("[authenticateRequest]: %s", loginRequest));
        return ResponseEntity.ok(new LoginResponse("Success", jwt));
    }
}
