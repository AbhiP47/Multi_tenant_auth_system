package com.project.Multi_tenant_auth_system.Controller;

import com.project.Multi_tenant_auth_system.Payload.JwtAuthenticationResponse;
import com.project.Multi_tenant_auth_system.Payload.LoginRequest;
import com.project.Multi_tenant_auth_system.Payload.SignUpRequest;
import com.project.Multi_tenant_auth_system.Service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        String jwt = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        try {
            authService.registerUser(signUpRequest);
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}