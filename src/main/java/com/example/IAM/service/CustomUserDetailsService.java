package com.example.IAM.service;

import com.example.IAM.CustomUserDetails;
import com.example.IAM.database.RoleRepository;
import com.example.IAM.database.UserRepository;
import com.example.IAM.model.AppUser;
import com.example.IAM.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repo;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AppUser> appUser = repo.findByEmail(email);
        Role role;
        if (appUser.isPresent() && appUser.get().isVerified()){
            role = roleRepository.findByName(appUser.get().getRole()).get();
            return new CustomUserDetails(appUser.get(), role.getPermissions());
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
