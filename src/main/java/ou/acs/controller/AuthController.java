package ou.acs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ou.acs.requests.LoginRequest;
import ou.acs.responses.LoginResponse;
import ou.acs.service.AuthService;

@RestController
@RequestMapping(path = "/api/auth", produces = "application/json")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signIn")
    public ResponseEntity<LoginResponse> signIn(@RequestBody LoginRequest loginRequest) {
        return authService.authenticateRequest(loginRequest);
    }
}
