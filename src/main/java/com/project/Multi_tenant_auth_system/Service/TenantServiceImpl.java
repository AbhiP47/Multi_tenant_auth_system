package com.project.Multi_tenant_auth_system.Service;


import com.project.Multi_tenant_auth_system.Entity.Tenant;
import com.project.Multi_tenant_auth_system.Payload.TenantRequest;
import com.project.Multi_tenant_auth_system.Repository.TenantRepository;
import com.project.Multi_tenant_auth_system.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.project.Multi_tenant_auth_system.Entity.User;

import java.util.List;
import java.util.Optional;

@Service
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;

    public TenantServiceImpl(TenantRepository tenantRepository, UserRepository userRepository) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    @Override
    public Optional<Tenant> getTenantById(Long id) {
        return tenantRepository.findById(id);
    }

    @Override
    public Tenant createTenant(TenantRequest tenantRequest) {
        Tenant tenant = new Tenant();
        tenant.setName(tenantRequest.getName());
        return tenantRepository.save(tenant);
    }

    @Override
    public Tenant updateTenant(Long id, TenantRequest tenantRequest) {
        Tenant existingTenant = tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tenant not found with id: " + id));
        existingTenant.setName(tenantRequest.getName());
        return tenantRepository.save(existingTenant);
    }

    @Override
    public void deleteTenant(Long id) {
        if (!tenantRepository.existsById(id)) {
            throw new RuntimeException("Tenant not found with id: " + id);
        }
        tenantRepository.deleteById(id);
    }

    @Override
    public Optional<Tenant> getCurrentUserTenant() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .map(User::getTenant);
    }
}