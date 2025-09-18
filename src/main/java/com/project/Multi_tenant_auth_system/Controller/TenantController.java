package com.project.Multi_tenant_auth_system.Controller;

import com.project.Multi_tenant_auth_system.Entity.Tenant;
import com.project.Multi_tenant_auth_system.Payload.TenantRequest;
import com.project.Multi_tenant_auth_system.Service.TenantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping("/my-tenant")
    public ResponseEntity<Tenant> getCurrentUserTenant() {
        return tenantService.getCurrentUserTenant()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Tenant> createTenant(@RequestBody TenantRequest tenantRequest) {
        Tenant newTenant = tenantService.createTenant(tenantRequest);
        return new ResponseEntity<>(newTenant, HttpStatus.CREATED);
    }


    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<Tenant>> getAllTenants() {
        return ResponseEntity.ok(tenantService.getAllTenants());
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Tenant> getTenantById(@PathVariable Long id) {
        return tenantService.getTenantById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Tenant> updateTenant(@PathVariable Long id, @RequestBody TenantRequest tenantRequest) {
        try {
            Tenant updatedTenant = tenantService.updateTenant(id, tenantRequest);
            return ResponseEntity.ok(updatedTenant);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> deleteTenant(@PathVariable Long id) {
        try {
            tenantService.deleteTenant(id);
            return ResponseEntity.ok("Tenant deleted successfully.");
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}