package com.project.Multi_tenant_auth_system.Service;

import com.project.Multi_tenant_auth_system.Entity.Tenant;
import com.project.Multi_tenant_auth_system.Payload.TenantRequest;

import java.util.List;
import java.util.Optional;

public interface TenantService {
    List<Tenant> getAllTenants();
    Optional<Tenant> getTenantById(Long id);
    Tenant createTenant(TenantRequest tenantRequest);
    Tenant updateTenant(Long id, TenantRequest tenantRequest);
    void deleteTenant(Long id);
    Optional<Tenant> getCurrentUserTenant();
}