package com.example.project.controller;

import com.example.project.entity.Collection;
import com.example.project.entity.Comment;
import com.example.project.entity.Item;
import com.example.project.entity.User;
import com.example.project.service.CollectionService;
import com.example.project.service.CommentService;
import com.example.project.service.CustomUserDetails;
import com.example.project.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ItemController {

    @Autowired
    CollectionService collectionService;

    @Autowired
    ItemService itemService;

    @Autowired
    CommentService commentService;

//    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
//    @GetMapping("/profile/{uid}/collections/{id}/items")
//    public String getCollectionItems(@PathVariable("id") int id, @PathVariable("uid") Long uid,Model model){
//        Collection collection = collectionService.get(id);
//        List<Item> items = itemService.getByCollectionId(id);
//        model.addAttribute("items", items);
//        model.addAttribute("collection", collection);
//        return "collection_items";
//    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @RequestMapping(value = "/profile/{uid}/collections/{id}/action", method = RequestMethod.GET, params = "add")
    public String newItem(@PathVariable("id") int id,@PathVariable("uid") Long uid, Model model){
        Collection collection = collectionService.get(id);
        model.addAttribute("item", new Item());
        model.addAttribute("collection", collection);
        return "new_item";
    }

    @GetMapping("/profile/{uid}/collections/{id}/view_item/{item_id}")
    public String viewItem(Model model,@PathVariable("item_id") int itemId)
    {   Item item=itemService.get(itemId);
        Comment comment=new Comment();
        List<Comment> comments=commentService.commentList(itemId);
        model.addAttribute("item",item);
        model.addAttribute("comment",comment);
        model.addAttribute("comments",comments);
        return "view_item";
    }

    @RequestMapping (value="/profile/{uid}/collections/{id}/view_item/{item_id}/add_comment", method={RequestMethod.POST,RequestMethod.GET})
    public String addComment(@PathVariable("item_id") int itemId,Comment comment)
    {
        CustomUserDetails myUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        comment.setUser(myUserDetails.getUser());
        comment.setItem(itemService.get(itemId));
        commentService.save(comment);
        return "redirect:/profile/{uid}/collections/{id}/view_item/{item_id}";
    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @PostMapping("/profile/{uid}/collections/{id}/add_item")
    public String addItem(@PathVariable("id") int id, @PathVariable("uid") Long uid, Item item){
        Collection collection = collectionService.get(id);
        item.setCollection(collection);
        itemService.update(item);
        return "redirect:/profile/"+uid+"/collections/"+ id + "/view";
    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @GetMapping("/profile/{uid}/collections/{id}/edit_item/{item_id}")
    public String editItem(@PathVariable("id") int id,@PathVariable("item_id") int itemId, @PathVariable("uid") Long uid,Model model){
        Collection collection = collectionService.get(id);
        Item item = itemService.get(itemId);
        model.addAttribute("collection", collection);
        model.addAttribute("item", item);
        return "edit_item";
    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @PostMapping("/profile/{uid}/collections/{id}/save_item/{item_id}")
    public String saveItem(@PathVariable("id") int id, @PathVariable("uid") Long uid,@PathVariable("item_id") int itemId,Item item){
        item.setId(itemId);
        item.setCollection(collectionService.get(id));
        itemService.update(item);
        return "redirect:/profile/"+uid+"/collections/"+ id + "/view";
    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @GetMapping("/profile/{uid}/collections/{id}/delete_item/{item_id}")
    public String deleteItemConfirmation(@PathVariable("id") int id,@PathVariable("item_id") int itemId, @PathVariable("uid") Long uid, Model model){
        Collection collection = collectionService.get(id);
        Item item = itemService.get(itemId);
        model.addAttribute("collection", collection);
        model.addAttribute("item", item);
        return "delete_item";
    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @PostMapping("/profile/{uid}/collections/{id}/delete_item/{item_id}")
    public String deleteItem(@PathVariable("id") int id, @PathVariable("uid") Long uid,@PathVariable("item_id") int itemId){
        itemService.delete(itemId);
        return "redirect:/profile/"+uid+"/collections/"+ id + "/view";
    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @RequestMapping(value = "/profile/{uid}/collections/{id}/action", method = {RequestMethod.POST,RequestMethod.GET}, params = "delete")
    public String deleteItems(@RequestParam(value="ids", required = false) List <Integer> ids, @PathVariable("id") int id, @PathVariable("uid") Long uid){
        if (ids != null)
            for (Integer itemId : ids)
                itemService.delete(itemId);
        return "redirect:/profile/"+uid+"/collections/"+ id + "/view";
    }

}
