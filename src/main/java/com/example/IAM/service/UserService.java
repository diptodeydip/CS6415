package com.example.IAM.service;

import com.example.IAM.Commons;
import com.example.IAM.DTO.AppUserDTO;
import com.example.IAM.DTO.PasswordChangeDTO;
import com.example.IAM.database.UserRepository;
import com.example.IAM.model.AppUser;
import com.example.IAM.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean changePassword(String email, PasswordChangeDTO passwordChangeDTO) {
        // Fetch the user by username
        Optional<AppUser> user = userRepository.findByEmail(email);

        // Check if the current password matches
        if (passwordEncoder.matches(passwordChangeDTO.getCurrentPassword(), user.get().getPassword())) {
            // Encode the new password and set it
            String encodedPassword = passwordEncoder.encode(passwordChangeDTO.getNewPassword());
            user.get().setPassword(encodedPassword);
            userRepository.save(user.get());
            return true;
        } else {
            return false; // Current password is incorrect
        }
    }

    public void updateProfile(String email, AppUserDTO appUserDTO){
        Optional<AppUser> user = userRepository.findByEmail(email);
        user.get().setFirstName(appUserDTO.getFirstName());
        user.get().setLastName(appUserDTO.getLastName());
        user.get().setEmail(appUserDTO.getEmail());
        userRepository.save(user.get());
    }

    public boolean isSameUser(Optional<Product> product, HttpSession session){
        return product.isPresent() && session.getAttribute(Commons.userId) == product.get().getOwnerId();
    }

    public boolean hasDeleteAccess(String permissions){
        String[] permissionArray = permissions.split(",\\s*");
        return Arrays.asList(permissionArray).contains(Commons.Delete_Any_Product_PERMISSION);
    }
}
