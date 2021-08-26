package com.example.project.controller;

import com.example.project.entity.Collection;
import com.example.project.entity.Item;
import com.example.project.entity.Topic;
import com.example.project.entity.User;
import com.example.project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private TopicService topicService;

//    @GetMapping("/collections")
//    public String collections(Model model) {
//        List<Collection> collections = collectionService.listAll();
//        model.addAttribute("collections", collections);
//        return "collections";
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
    public String showNewCollectionForm(@PathVariable("uid") Long uid, Model model){
        List<Topic> topics=topicService.listAll();
        Collection collection=new Collection();
        Topic topic=new Topic();
        collection.setOwner(service.get(uid));
        model.addAttribute("topic",topic);
        model.addAttribute("topics",topics);
        model.addAttribute("collection", collection);
        return "collection_form";
    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @PostMapping("profile/{uid}/collections/add")
    public String addNewCollection(@PathVariable("uid") Long uid, @RequestParam(value="userId") Long userId,Collection collection){
        collection.setOwner(service.get(userId));
        collectionService.update(collection);
        return "redirect:/profile/{uid}";
    }



    //@PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @GetMapping("profile/{uid}/collections/{id}/view")
    public String viewCollection(@PathVariable("id") int id, @PathVariable("uid") Long uid, Model model){
        Collection collection = collectionService.get(id);
        List<Collection> collections=collectionService.collectionList(uid);
        List<Item> items = itemService.getByCollectionId(id);
        List<Topic> topics=topicService.listAll();
        User user=service.get(uid);
        model.addAttribute("user",user);
        model.addAttribute("items", items);
        model.addAttribute("topics",topics);
        model.addAttribute("collection", collection);
        model.addAttribute("collections",collections);
        return "view_collection";

    }
//    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
//    @GetMapping("profile/{uid}/collections/{id}/edit")
//    public String editCollection(@PathVariable("id") int id, @PathVariable("uid") Long uid,Model model){
//        Collection collection = collectionService.get(id);
//        model.addAttribute("collection", collection);
//        return "edit_collection";
//    }

//    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
//    @PostMapping("profile/{uid}/collections/{id}/save")
//    public String saveCollection(@PathVariable("id") int id,  @PathVariable("uid") Long uid, Collection collection){
//        System.out.println(collection==null);
//        collection.setOwner(service.get(uid));
//        collectionService.update(collection);
//        return "redirect:/profile/"+uid;
//    }


//    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
//    @PostMapping("profile/{uid}/collections/{id}/delete")
//    public String deleteCollection(@PathVariable("uid") Long uid,@PathVariable("id") int id){
//       // collectionService.delete(id);
//        List<Item> items=itemService.getByCollectionId(id);
//        for (Item item:items) itemService.delete(item.getId());
//        collectionService.delete(id);
//        return "redirect:/profile/"+uid;
//    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @RequestMapping(value = "profile/{uid}/collections/{id}/view", method = RequestMethod.POST, params = "save")
    public String saveCollection(@PathVariable("id") int id,  @PathVariable("uid") Long uid, Collection collection){
        System.out.println(collection==null);
        collection.setOwner(service.get(uid));
        collectionService.update(collection);
        return "redirect:/profile/"+uid;
    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @RequestMapping(value = "profile/{uid}/collections/{id}/view", method = RequestMethod.POST, params = "delete")
    public String deleteCollection(@PathVariable("uid") Long uid,@PathVariable("id") int id){
        // collectionService.delete(id);
        List<Item> items=itemService.getByCollectionId(id);
        for (Item item:items) itemService.delete(item.getId());
        collectionService.delete(id);
        return "redirect:/profile/"+uid;
    }
}

