package com.project.Multi_tenant_auth_system.Payload;

import lombok.Data;

@Data
public class SignUpRequest {
    private String username;
    private String password;
    private Long tenantId;
}
