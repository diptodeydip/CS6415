package com.example.IAM.controller;

import com.example.IAM.Commons;
import com.example.IAM.DTO.ProductDTO;
import com.example.IAM.DTO.RoleDTO;
import com.example.IAM.database.RoleRepository;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class AdminController {

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("iam")
    public String index(Model model) {
        return "iam/iam";
    }

    @GetMapping("user-requests")
    public String requests(Model model) {
        return "iam/user_requests";
    }

    @GetMapping("role-management")
    public String role(Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("roleDTO", new RoleDTO());
        return "iam/role_management";
    }

    @PostMapping("create-role")
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
}
