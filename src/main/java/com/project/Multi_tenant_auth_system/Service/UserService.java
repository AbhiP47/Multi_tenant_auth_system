package com.project.Multi_tenant_auth_system.Service;

import com.project.Multi_tenant_auth_system.Entity.User;
import com.project.Multi_tenant_auth_system.Payload.UpdateUserRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User updateUser(Long id , UpdateUserRequest updateUserRequest);
    void deleteUserById(Long id);

}
