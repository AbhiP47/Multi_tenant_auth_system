package com.project.Multi_tenant_auth_system.Repository;
import com.project.Multi_tenant_auth_system.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    List<User> findByTenantId(Long id);
}
