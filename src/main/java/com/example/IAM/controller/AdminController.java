package com.example.IAM.controller;

import com.example.IAM.Commons;
import com.example.IAM.DTO.ProductDTO;
import com.example.IAM.DTO.RoleDTO;
import com.example.IAM.database.RoleRepository;
import com.example.IAM.database.UserRepository;
import com.example.IAM.model.AppUser;
import com.example.IAM.model.Product;
import com.example.IAM.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/iam")
    public String index(Model model) {
        return "iam/iam";
    }

    @GetMapping("/user-requests")
    public String requests(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "iam/user_requests";
    }

    @GetMapping("/iam/reject-request/{id}")
    public String reject(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        AppUser appUser = userRepository.findById(id).get();
        appUser.setVerified(false);
        userRepository.save(appUser);
        redirectAttributes.addFlashAttribute("message", "User is locked");
        return "redirect:/user-requests";
    }

    @GetMapping("/iam/approve-request/{id}")
    public String approve(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        AppUser appUser = userRepository.findById(id).get();
        appUser.setVerified(true);
        userRepository.save(appUser);
        redirectAttributes.addFlashAttribute("message", "User is verified");
        return "redirect:/user-requests";
    }

    @GetMapping("/role-management")
    public String role(Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("roleDTO", new RoleDTO());
        return "iam/role_management";
    }

    @PostMapping("/iam/create-role")
    public String addProduct(Model model, @ModelAttribute RoleDTO roleDTO, RedirectAttributes redirectAttributes) {

        Optional<Role> role = roleRepository.findByName(roleDTO.getName());
        if (role.isPresent())
        {
            redirectAttributes.addFlashAttribute("errorMessage", "Role already exists!");
            return "redirect:/role-management";
        }

        Role newRole = new Role();
        newRole.setName(roleDTO.getName());
        newRole.setPermissions(roleDTO.getPermissions());
        roleRepository.save(newRole);

        redirectAttributes.addFlashAttribute("message", "Role created successfully!");
        return "redirect:/role-management";
    }

    @GetMapping("/iam/role-management/edit/{id}")
    public String editPage(Model model, @PathVariable Long id) {
        Role role = roleRepository.findById(id).get();
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setPermissions(role.getPermissions());

        model.addAttribute("roleDTO", roleDTO);
        return "iam/update-role";
    }

    @PostMapping("/iam/edit-role")
    public String editRole(Model model, @ModelAttribute RoleDTO roleDTO, RedirectAttributes redirectAttributes) {
        Role role = new Role();
        role.setPermissions(roleDTO.getPermissions());
        role.setName(roleDTO.getName());
        role.setId(roleDTO.getId());
        roleRepository.save(role);

        redirectAttributes.addFlashAttribute("message", "Role updated successfully!");
        return "redirect:/role-management";
    }
}
