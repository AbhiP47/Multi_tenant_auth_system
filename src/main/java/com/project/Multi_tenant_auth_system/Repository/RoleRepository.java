package com.project.Multi_tenant_auth_system.Repository;

import com.project.Multi_tenant_auth_system.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository  extends JpaRepository<Role,Integer> {

    Optional<Role> findByName(String name);
}
