package com.example.project.controller;

import com.example.project.entity.Collection;
import com.example.project.entity.Item;
import com.example.project.entity.User;
import com.example.project.service.CollectionService;
import com.example.project.service.CustomUserDetails;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Set;

@Controller
public class CollectionController {
    @Autowired
    private CollectionService collectionService;

    @Autowired
    private UserService service;

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

    @GetMapping("/collections/{id}/delete")
    public String deleteCollection(@PathVariable("id") int id, Model model){
        Collection collection = collectionService.get(id);
        model.addAttribute("collection", collection);
        return "delete_collection";
    }

    @PostMapping("/collections/delete")
    public String deleteCollection(Collection collection){
        collectionService.delete(collection);
        return "redirect:/collections";
    }
}
