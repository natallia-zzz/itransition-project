package com.example.project.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import com.example.project.entity.Role;
import com.example.project.entity.User;
import com.example.project.service.CustomUserDetails;
import com.example.project.service.UserService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AppController {
    @Resource(name = "sessionRegistry")
    private SessionRegistry sessionRegistry;

    @Autowired
    private UserService service;

    @GetMapping("")
    public String viewHomePage(Model model) {
        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers);
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
            service.registerDefaultUser(user);
            return "register_success";
        //return "redirect:/register";
    }



    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers);

        return "users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        User user = service.get(id);
        List<Role> listRoles = service.listRoles();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        return "user_form";
    }

    @GetMapping("/profile/{id}")
    public String showProfile(@PathVariable("id") Long id, Model model) {
        User user = service.get(id);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/profile")
    public String showMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Long id=userDetails.getId();
        User user = service.get(id);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/users/save")
    public String saveUser(User user) {
        service.save(user);

        return "redirect:/users";
    }

    @GetMapping("/users/select")
    public String select(@RequestParam(value="ids") List<Long> ids,Model model){
        model.addAttribute("ids",ids);
        return "redirect:/users/select";
    }

    @RequestMapping(value = "/users/select", method = RequestMethod.POST, params = "delete")
    public String delete(@RequestParam(value="ids") List<Long> ids,HttpServletRequest request )
    {
        if(ids != null){
            for(Long id : ids) {
                this.sessionExpire(request,id);
                service.delete(id);}
        }
        return "redirect:/users";
    }
    @RequestMapping(value = "/users/select", method = RequestMethod.POST, params = "toAdmin")
    public String admin(@RequestParam(value="ids") List<Long> ids, HttpServletRequest request)
    {
        if(ids != null){
            for(Long id : ids){
                if(service.checkRole(service.get(id)))
                {
                    service.deleteRole(service.get(id));
                    this.sessionExpire(request,id);
                }
                else service.addRole(service.get(id));
            }
        }
        return "redirect:/users";
    }
    @RequestMapping(value = "/users/select", method = RequestMethod.POST, params = "block")
    public String block(@RequestParam(value="ids") List<Long> ids,HttpServletRequest request) {
        if (ids != null) {
            for (Long id : ids) {
                service.changeStatus(service.get(id));
                this.sessionExpire(request, id);
            }
        }
        return "redirect:/users";
    }

    public void sessionExpire(HttpServletRequest request, Long id)
    {
        List<Object> users = sessionRegistry.getAllPrincipals();
        String username= service.get(id).getEmail();
        for (Object user : users) {
            List<SessionInformation> sessions = sessionRegistry.getAllSessions(user, false);
            for (SessionInformation session : sessions) {
                UserDetails principal=(UserDetails)session.getPrincipal();
                if (principal.getUsername().equals(username))
                    session.expireNow();
            }
        }
    }
}
