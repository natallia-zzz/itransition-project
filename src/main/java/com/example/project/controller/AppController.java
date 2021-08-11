package com.example.project.controller;

import java.util.List;

import com.example.project.entity.Role;
import com.example.project.entity.User;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class AppController {

    @Autowired
    private UserService service;

    @GetMapping("")
    public String viewHomePage() {
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
    public String delete(@RequestParam(value="ids") List<Long> ids )
    {
        if(ids != null){
            for(Long id : ids){
                service.delete(id);
            }
        }
        return "redirect:/users";
    }
    @RequestMapping(value = "/users/select", method = RequestMethod.POST, params = "toAdmin")
    public String admin(@RequestParam(value="ids") List<Long> ids, HttpSession session)
    {
        if(ids != null){
            for(Long id : ids){
                if(service.checkRole(service.get(id)))
                {
                    service.deleteRole(service.get(id));
                    session.invalidate();
                }
                else service.addRole(service.get(id));
            }
        }
        return "redirect:/users";
    }
    @RequestMapping(value = "/users/select", method = RequestMethod.POST, params = "block")
    public String block(@RequestParam(value="ids") List<Long> ids, HttpSession session)
    {
        if(ids!=null)
        {
            for(Long id : ids)
            {
                service.changeStatus(service.get(id),session);
            }
        }
        return "redirect:/users";
    }
}
