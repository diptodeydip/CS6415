package com.example.IAM.controller;
import com.example.IAM.Commons;
import com.example.IAM.DTO.AppUserDTO;
import com.example.IAM.DTO.OtpCode;
import com.example.IAM.database.RoleRepository;
import com.example.IAM.database.UserRepository;
import com.example.IAM.model.AppUser;
import com.example.IAM.service.CacheService;
import com.example.IAM.service.EmailService;
import com.example.IAM.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private EmailService emailService;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("index")
    public String index(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Fetch logged-in username
        Optional<AppUser> appUser = repo.findByEmail(email);

        // Store username in session
        session.setAttribute(Commons.name, appUser.get().getFirstName()+ " " + appUser.get().getLastName());
        session.setAttribute(Commons.userId, appUser.get().getId());
        session.setAttribute(Commons.role, appUser.get().getRole());

        return "index";
    }

    @GetMapping("mfa")
    public String mfa(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Fetch logged-in username
        String otp = emailService.generateOTP();
        cacheService.putData(email, Integer.valueOf(otp));
        emailService.sendOtp(email, otp);
        model.addAttribute(new OtpCode());
        return "mfa";
    }

    @PostMapping("otp")
    public String mfa(@ModelAttribute OtpCode otpCode, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Fetch logged-in username

        if (cacheService.getData(email) != null && cacheService.getData(email).toString().equals(otpCode.getCode())) {
            // Get existing roles
            List<GrantedAuthority> updatedAuthorities = authentication.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toList());
            // Add new role
            updatedAuthorities.add(new SimpleGrantedAuthority(Commons.MFA_VERIFIED));
            // Create new authentication token
            Authentication newAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
                    authentication.getCredentials(), updatedAuthorities);
            // Update Security Context
            SecurityContextHolder.getContext().setAuthentication(newAuth);

            return "redirect:/index";
        }

        redirectAttributes.addFlashAttribute("message", "Invalid OTP");
        return "redirect:/mfa";
    }

    @GetMapping("loginPage")
    public String loginPage(HttpServletRequest request, Model model) {
        String ipAddress = request.getRemoteAddr();
        if (cacheService.getData(ipAddress) != null && cacheService.getData(ipAddress)>= 5) {
            model.addAttribute("locked", true);
            model.addAttribute("ip", ipAddress);
        }
        return "login_page";
    }

    @GetMapping("loginError")
    public String loginError(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        String ipAddress = request.getRemoteAddr();  // Get request sender's IP

        if (cacheService.getData(ipAddress) != null ) {
            cacheService.putData(ipAddress, cacheService.getData(ipAddress)+1);
        }else {
            cacheService.putData(ipAddress, 1);
        }

        redirectAttributes.addFlashAttribute("message", "Something went wrong");
        return "redirect:/loginPage";
    }


    @GetMapping("registerPage")
    public String register(Model model) {
        AppUserDTO appUserDTO = new AppUserDTO();
        model.addAttribute(appUserDTO);
        model.addAttribute("items", roleRepository.findAll());
        return "register_page";
    }

    @PostMapping("register")
    public String processRegister(Model model, @Valid @ModelAttribute AppUserDTO appUserDTO
    , BindingResult result, RedirectAttributes redirectAttributes) {

        if (!appUserDTO.getPassword().equals(appUserDTO.getConfirmPassword()))
        {
            result.addError(
                    new FieldError("appUserDTO", "confirmPassword",
                            "Password didn't match")
            );
        }

        Optional<AppUser> appUser = repo.findByEmail(appUserDTO.getEmail());
        if (appUser.isPresent())
        {
            result.addError(
                    new FieldError("appUserDTO", "email",
                            "Email address is already used")
            );
        }

        if (result.hasErrors()){
            return "register_page";
        }

        try {
            AppUser newUser = new AppUser();
            newUser.setFirstName(appUserDTO.getFirstName());
            newUser.setLastName(appUserDTO.getLastName());
            newUser.setEmail(appUserDTO.getEmail());
            newUser.setCreatedAt(new Date());
            newUser.setPassword(passwordEncoder.encode(appUserDTO.getPassword()));
            newUser.setVerified(false);
            newUser.setRole(appUserDTO.getRole());

            repo.save(newUser);

        }catch (Exception e){
            result.addError(
                    new FieldError("appUserDTO", "firstName",
                            e.getMessage())
            );
            return "register_page";
        }

        redirectAttributes.addFlashAttribute("message", "Account created successfully!");
        return "redirect:/loginPage";
    }

}