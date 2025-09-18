package com.project.Multi_tenant_auth_system.Service;

import com.project.Multi_tenant_auth_system.Entity.User;
import com.project.Multi_tenant_auth_system.Repository.UserRepository;
import com.project.Multi_tenant_auth_system.Security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static  final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private UserRepository userRepository;
    @Autowired
    CustomUserDetailsService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load the user -> "+username);
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            return new CustomUserDetails(user);
        }
        else {
            throw new UsernameNotFoundException("user not found by the username "+username);
        }
    }
}
