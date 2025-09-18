package com.project.Multi_tenant_auth_system.Service;
import com.project.Multi_tenant_auth_system.Entity.Role;
import com.project.Multi_tenant_auth_system.Entity.Tenant;
import com.project.Multi_tenant_auth_system.Entity.User;
import com.project.Multi_tenant_auth_system.Payload.LoginRequest;
import com.project.Multi_tenant_auth_system.Payload.SignUpRequest;
import com.project.Multi_tenant_auth_system.Repository.RoleRepository;
import com.project.Multi_tenant_auth_system.Repository.TenantRepository;
import com.project.Multi_tenant_auth_system.Repository.UserRepository;
import com.project.Multi_tenant_auth_system.Security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository, RoleRepository roleRepository,
                           TenantRepository tenantRepository, PasswordEncoder passwordEncoder,
                           JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tenantRepository = tenantRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public String authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken(authentication);
    }

    @Override
    public User registerUser(SignUpRequest signUpRequest) {
        if (userRepository.findByUsername(signUpRequest.getUsername()).isPresent()) {
            throw new IllegalStateException("Username is already taken!");
        }

        Tenant tenant = tenantRepository.findById(signUpRequest.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found!"));

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setTenant(tenant);

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Default Role is not found."));
        user.setRoles(Collections.singleton(userRole));

        return userRepository.save(user);
    }
}