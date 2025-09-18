package com.project.Multi_tenant_auth_system.Service;

import com.project.Multi_tenant_auth_system.Entity.Role;
import com.project.Multi_tenant_auth_system.Entity.User;
import com.project.Multi_tenant_auth_system.Payload.UpdateUserRequest;
import com.project.Multi_tenant_auth_system.Repository.RoleRepository;
import com.project.Multi_tenant_auth_system.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository,RoleRepository roleRepository)
    {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public List<User> getAllUsers() {
        User user = getCurrentUser();
        if(isSuperAdmin(user))
        {
            logger.info("Getting users for Super Admin");
            return userRepository.findAll();
        }
        else if(isAdmin(user))
        {
            logger.info("Getting users for Admin");
            return userRepository.findByTenantId(user.getTenant().getId());
        }
        else
        {
            logger.info("You are not allowed to see the user list");
            return Collections.EMPTY_LIST;

        }
    }

    @Override
    public Optional<User> getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        User currentUser = getCurrentUser();
        if(userOptional.isEmpty())
        {
            logger.info("User not found by the username "+userOptional.get().getUsername());
            return Optional.empty();
        }
        User user = userOptional.get();
        if(isSuperAdmin(currentUser))
        {
            logger.info("Getting user for Super Admin");
            return Optional.of(user);
        }
        else if(isAdmin(currentUser) && user.getTenant().getId().equals(currentUser.getTenant().getId()))
        {
            logger.info("Getting user for Admin of its Tenant");
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public User updateUser(Long id, UpdateUserRequest updateUserRequest) {
        User currentUser = getCurrentUser();
        User userToBeUpdated = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with id "+id));

        if(!isSuperAdmin(currentUser) && !currentUser.getTenant().getId().equals(userToBeUpdated.getTenant().getId()))
        {
            throw new AccessDeniedException("You do not have permission to update this user.");
        }
        userToBeUpdated.setUsername(updateUserRequest.getUsername());

        //to update the password
        if(updateUserRequest.getPassword()!= null && !updateUserRequest.getPassword().isEmpty())
        {
            userToBeUpdated.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        }
        // to update the roles
        if (updateUserRequest.getRoles() != null) {
            Set<Role> newRoles = updateUserRequest.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new RuntimeException("Error: Role not found - " + roleName)))
                    .collect(Collectors.toSet());
            userToBeUpdated.setRoles(newRoles);
        }
        return userRepository.save(userToBeUpdated);
    }


    @Override
    public void deleteUserById(Long id) {
        User currentUser = getCurrentUser();
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        if (!isSuperAdmin(currentUser) && !userToDelete.getTenant().getId().equals(currentUser.getTenant().getId())) {
            throw new AccessDeniedException("You do not have permission to delete this user.");
        }

        userRepository.deleteById(id);

    }

    private User getCurrentUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found in the database"));
    }

    private boolean isSuperAdmin(User user)
    {
        boolean result = user.getRoles().stream().anyMatch(role -> role.getRole().equals("ROLE_SUPER_ADMIN"));
                return result;
    }

    private boolean isAdmin(User user)
    {
        boolean result = user.getRoles().stream().anyMatch(role -> role.getRole().equals("ROLE_ADMIN"));
        return result;
    }

}
