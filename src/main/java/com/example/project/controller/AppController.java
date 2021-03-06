package com.example.project.controller;

import java.security.Principal;
import java.util.ArrayList;

import java.util.HashSet;

import java.util.List;
import java.util.Map;
import java.util.Set;


import com.example.project.entity.Role;
import com.example.project.entity.User;
import com.example.project.oath.CustomOAuth2User;
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

import com.example.project.entity.*;
import com.example.project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
public class AppController {
    @Resource(name = "sessionRegistry")
    private SessionRegistry sessionRegistry;

    @Autowired
    private UserService service;

    @Autowired
    private TopicService topicService;

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private ItemService itemService;
    @Autowired
    private TagService tagService;

    @GetMapping("")
    public String viewHomePage(Model model) {
        this.addUsers(model);
        model.addAttribute("filter", new Filter());
        this.showAll(model);
        return "index";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("filter", new Filter());
        return "signup_form";
    }
    @GetMapping("/tags/{tag_id}")
    public String showTagItems(@PathVariable("tag_id") int tagId,Model model)
    {
        model.addAttribute("filter", new Filter());
        List<Item> searchItems=itemService.listItemsByTagId(tagId);
        model.addAttribute("searchItems", searchItems);
        this.showAll(model);
        return "index";
    }

    @PostMapping("/process_register")
    public String processRegister(User user, Model model) {
        model.addAttribute("filter", new Filter());
        service.registerDefaultUser(user);
        return "register_success";
        //return "redirect:/register";
    }



    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("filter", new Filter());
        List<User> listUsers = service.listAll();
        List<Topic> listTopics=topicService.listAll();
        model.addAttribute("listTopics",listTopics);
        model.addAttribute("listUsers", listUsers);

        return "users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("filter", new Filter());
        User user = service.get(id);
        List<Role> listRoles = service.listRoles();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        return "user_form";
    }

    public void addUsers(Model model)
    {
        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers);
    }
    public void addTags(Model model)
    {
        List<Tag> tags = tagService.tagList();
        model.addAttribute("tags", tags);
    }

    public void addItems(Model model)
    {
        List<Item> items = itemService.findLatest();
        model.addAttribute("items", items);
    }
    public void addCollections(Model model)
    {
        List<Collection> cols=collectionService.getTop5();
        model.addAttribute("cols", cols);
    }
    public void showAll(Model model)
    {
        this.addUsers(model);
        this.addItems(model);
        this.addCollections(model);
        this.addTags(model);
    }
    @GetMapping("/profile/{id}")
    public String showProfile(@PathVariable("id") Long id, Model model) {
        model.addAttribute("filter", new Filter());
        User user = service.get(id);
        this.showAll(model);
        List<Collection> collections = service.listCollections(user);
        model.addAttribute("collections", collections);
        model.addAttribute("user", user);
        return "index";
    }

    @GetMapping("/profile")
    public String showMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("filter", new Filter());
        Long id=userDetails.getId();
        User user = service.get(id);
        List<Collection> collections = service.listCollections(user);
        this.showAll(model);
        model.addAttribute("collections", collections);
        model.addAttribute("user", user);
        return "index";
    }


    @PostMapping("/users/save")
    public String saveUser(User user) {
        service.save(user);
        return "redirect:/users";
    }

    @GetMapping("/users/select")
    public String select(@RequestParam(value="ids") List<Long> ids,Model model){
        model.addAttribute("filter", new Filter());
        model.addAttribute("ids",ids);
        return "redirect:/users/select";
    }

    @RequestMapping(value = "/users/select", method = RequestMethod.POST, params = "delete")
    public String delete(@RequestParam(value="ids") List<Long> ids,HttpServletRequest request )
    {
        if(ids != null){
            for(Long id : ids) {
                this.sessionExpire(request,id);
                for(Collection col:collectionService.collectionList(id))
                    collectionService.delete(col.getId());
                service.delete(id);
            }
        }
        return "redirect:/users";
    }

    @RequestMapping(value="/users/topics/action",method = {RequestMethod.GET,RequestMethod.POST}, params = "add")
    public String addTopic(Model model)
    {
        model.addAttribute("filter", new Filter());
        model.addAttribute("topic",new Topic());
        return "new_topic";
    }
    @RequestMapping(value="/users/topics/action", method = {RequestMethod.POST,RequestMethod.GET}, params = "delete")
    public String deleteItems(@RequestParam(value="ids", required = false) List <Integer> ids){
        if (ids != null)
            for (Integer itemId : ids) {
                for (Collection col : collectionService.collectionListTopic(itemId)) col.setTopic(null);
                topicService.delete(itemId);
            }
        return "redirect:/users";
    }

    @PostMapping("/users/topics/add_topic")
    public String addItem(Topic topic){
        topicService.update(topic);
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

