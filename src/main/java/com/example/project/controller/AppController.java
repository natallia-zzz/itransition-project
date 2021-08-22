package com.example.project.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.project.entity.Collection;
import com.example.project.entity.Item;
import com.example.project.entity.Role;
import com.example.project.entity.User;
import com.example.project.service.CollectionService;
import com.example.project.service.CustomUserDetails;
import com.example.project.service.UserService;
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

    @Autowired
    private UserService service;

    @Autowired
    private CollectionService collectionService;

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

    @GetMapping("/collections")
    public String collections(Model model) {
        List<Collection> collections = collectionService.listAll();
        model.addAttribute("collections", collections);
        return "collections";
    }

    @GetMapping("/collections/{id}")
    public String userCollections(@PathVariable("id") Long id, Model model) {
        User user = service.get(id);
        List<Collection> collections = service.listCollections();
        model.addAttribute("collections", collections);
        model.addAttribute("owner", user);
        return "user_collection";
    }

    @PostMapping("/collections/delete")
    public String deleteCollection(Collection collection){
        collectionService.delete(collection);
        return "redirect:/collections";
    }

    @GetMapping("/collections/new")
    public String showNewCollectionForm(Model model){
        model.addAttribute("collection", new Collection());
        return "collection_form";
    }

    @PostMapping("/collection/add")
    public String addNewCollection(Collection collection){
        CustomUserDetails myUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        collection.setOwner(myUserDetails.getUser());
        collectionService.update(collection);
        return "redirect:/collections";
    }

    @GetMapping("/collections/{id}/view")
    public String viewCollection(@PathVariable("id") int id,Model model){
        Collection collection = collectionService.get(id);
        model.addAttribute("collection", collection);
        return "view_collection";

    }

    @GetMapping("/collections/{id}/edit")
    public String editCollection(@PathVariable("id") int id,Model model){
        Collection collection = collectionService.get(id);
        model.addAttribute("collection", collection);
        return "edit_collection";
    }

    @PostMapping("/collections/{id}/save")
    public String saveCollection(@PathVariable("id") int id, Collection collection){
        CustomUserDetails myUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        collection.setOwner(myUserDetails.getUser());
        Set<Item> items = collection.getItems();
        if(items != null){
            collection.getItems().clear();
            collection.getItems().addAll(items);
        }
        collection.setItems(items);
        collectionService.update(collection);
        return "redirect:/collections";
    }


    @PostMapping("/collections/{id}/delete")
    public String deleteCollection(@PathVariable("id") int id, Collection collection){
        collectionService.delete(collection);
        return "redirect:/collections";
    }
}