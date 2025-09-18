package com.project.Multi_tenant_auth_system.Repository;

import com.project.Multi_tenant_auth_system.Entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant,Long> {
}
