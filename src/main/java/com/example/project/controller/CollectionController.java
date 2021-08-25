package com.example.project.controller;

import com.example.project.entity.Collection;
import com.example.project.entity.Item;
import com.example.project.entity.User;
import com.example.project.service.CollectionService;
import com.example.project.service.CustomUserDetails;
import com.example.project.service.ItemService;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Controller
public class CollectionController {
    @Autowired
    private CollectionService collectionService;

    @Autowired
    private UserService service;

    @Autowired
    private ItemService itemService;

//    @GetMapping("/collections")
//    public String collections(Model model) {
//        List<Collection> collections = collectionService.listAll();
//        model.addAttribute("collections", collections);
//        return "collections";
//    }

//    @GetMapping("/collections/new")
//    public String showNewCollectionForm(Model model) {
//        model.addAttribute("collection", new Collection());
//
//    }
//    @GetMapping("/collections/{id}")
//    public String userCollections(@PathVariable("id") Long id, Model model) {
//        User user = service.get(id);
//        List<Collection> collections = service.listCollections();
//        model.addAttribute("collections", collections);
//        model.addAttribute("owner", user);
//        return "user_collection";
//    }

//    @PostMapping("/collections/delete")
//    public String deleteCollection(Collection collection){
//        collectionService.delete(collection);
//        return "redirect:/collections";
//    }

        @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
        @GetMapping("profile/{uid}/collections/new")
        public String showNewCollectionForm (@PathVariable("uid") Long uid, Model model){
            Collection collection = new Collection();
            collection.setOwner(service.get(uid));
            model.addAttribute("collection", collection);
            return "collection_form";
        }

        @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
        @PostMapping("profile/{uid}/collections/add")
        public String addNewCollection (@PathVariable("uid") Long uid, @RequestParam(value = "userId") Long
        userId, Collection collection){
            collection.setOwner(service.get(userId));

            collectionService.update(collection);
            return "redirect:/profile/{uid}";
        }


        @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
        @GetMapping("profile/{uid}/collections/{id}/view")
        public String viewCollection ( @PathVariable("id") int id, @PathVariable("uid") Long uid, Model model){
            Collection collection = collectionService.get(id);
            model.addAttribute("collection", collection);
            return "view_collection";

        }

        @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
        @GetMapping("profile/{uid}/collections/{id}/edit")
        public String editCollection ( @PathVariable("id") int id, @PathVariable("uid") Long uid, Model model){
            Collection collection = collectionService.get(id);
            model.addAttribute("collection", collection);
            return "edit_collection";
        }

        @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
        @PostMapping("profile/{uid}/collections/{id}/save")
        public String saveCollection ( @PathVariable("id") int id, @PathVariable("uid") Long uid, Collection collection)
        {
            collection.setOwner(service.get(uid));
            collectionService.update(collection);
            return "redirect:/profile/" + uid;
        }

        @GetMapping("/collections/{id}/delete")
        public String deleteCollection ( @PathVariable("id") int id, Model model){
            Collection collection = collectionService.get(id);
            model.addAttribute("collection", collection);
            return "delete_collection";
        }

        @PostMapping("/collections/delete")
        public String deleteCollection (Collection collection) {
            collectionService.delete(collection.getId());
            return "redirect:/collections";
        }

        @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
        @PostMapping("profile/{uid}/collections/{id}/delete")
        public String deleteCollection (@PathVariable("uid") Long uid,@PathVariable("id") int id){
            // collectionService.delete(id);
            List<Item> items = itemService.getByCollectionId(id);
            for (Item item : items) itemService.delete(item.getId());
            collectionService.delete(id);
            return "redirect:/profile/" + uid;
        }
    }