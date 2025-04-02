package com.example.IAM;

import com.example.IAM.database.RoleRepository;
import com.example.IAM.model.AppUser;
import com.example.IAM.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    @Autowired
    RoleRepository roleRepository;

    private AppUser user;

    private String permissions;

    public CustomUserDetails(AppUser user, String permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        String[] permissionArray = permissions.split(",\\s*"); // Split by comma and optional spaces
        for (String permission : permissionArray) {
            if (!permission.isEmpty()){
                GrantedAuthority authority = new SimpleGrantedAuthority(permission);
                grantedAuthorities.add(authority);
            }
        }

        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        if(user != null) {
            return user.getPassword();
        }
        return "";
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public AppUser getUser(){
        return user;
    }

}