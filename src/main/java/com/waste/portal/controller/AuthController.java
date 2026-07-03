package com.waste.portal.controller;

import com.waste.portal.model.User;
import com.waste.portal.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
        }
        return "home";
    }

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            var authorities = userDetails.getAuthorities();
            for (var auth : authorities) {
                if (auth.getAuthority().equals("ROLE_ADMIN")) {
                    return "redirect:/admin/dashboard";
                } else if (auth.getAuthority().equals("ROLE_STAFF")) {
                    return "redirect:/staff/dashboard";
                } else if (auth.getAuthority().equals("ROLE_CITIZEN")) {
                    return "redirect:/citizen/dashboard";
                }
            }
        }
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            return "redirect:/";
        }
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.registerCitizen(user);
            return "redirect:/login?registered=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
