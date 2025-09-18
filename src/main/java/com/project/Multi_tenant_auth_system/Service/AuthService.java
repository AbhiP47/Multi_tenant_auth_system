package com.project.Multi_tenant_auth_system.Service;

import com.project.Multi_tenant_auth_system.Entity.User;
import com.project.Multi_tenant_auth_system.Payload.LoginRequest;
import com.project.Multi_tenant_auth_system.Payload.SignUpRequest;

public interface AuthService {
    User registerUser(SignUpRequest signUpRequest);
    String authenticateUser(LoginRequest loginRequest);
}