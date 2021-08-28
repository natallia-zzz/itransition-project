package com.example.project.controller;

import com.example.project.entity.Filter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("filter", new Filter());
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || authentication instanceof AnonymousAuthenticationToken)
        {
            return "login";
        }
        return "redirect:/";
    }

}