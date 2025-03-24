package com.example.IAM.service;

import com.example.IAM.CustomUserDetails;
import com.example.IAM.database.UserRepository;
import com.example.IAM.model.AppUser;
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AppUser> appUser = repo.findByEmail(email);

        if (appUser.isPresent() && appUser.get().isVerified()){
            return new CustomUserDetails(appUser.get());
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
