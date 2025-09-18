package com.project.Multi_tenant_auth_system.Payload;

import lombok.Data;

import java.util.Set;

@Data
public class UpdateUserRequest {
    private String username;
    private String password;
    private Set<String> roles;

}
