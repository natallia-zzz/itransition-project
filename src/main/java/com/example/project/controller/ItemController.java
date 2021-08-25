package com.example.project.controller;

import com.example.project.entity.Collection;
import com.example.project.entity.Item;
import com.example.project.entity.Role;
import com.example.project.entity.Tag;
import com.example.project.service.CollectionService;
import com.example.project.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Controller
public class ItemController {

    @Autowired
    CollectionService collectionService;

    @Autowired
    ItemService itemService;

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @GetMapping("/profile/{uid}/collections/{id}/items")
    public String getCollectionItems(@PathVariable("id") int id, @PathVariable("uid") Long uid,Model model){
        Collection collection = collectionService.get(id);
        List<Item> items = itemService.getByCollectionId(id);
        model.addAttribute("items", items);
        model.addAttribute("collection", collection);
        return "collection_items";
    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @GetMapping("/profile/{uid}/collections/{id}/new_item")
    public String newItem(@PathVariable("id") int id,@PathVariable("uid") Long uid, Model model){
        Collection collection = collectionService.get(id);
        model.addAttribute("item", new Item());
        model.addAttribute("collection", collection);
        List<Tag> listTags = itemService.listTags();
        model.addAttribute("listTags", listTags);
        model.addAttribute("other", "");
        return "new_item";
    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @PostMapping("/profile/{uid}/collections/{id}/add_item")
    public String addItem(@PathVariable("id") int id, @PathVariable("uid") Long uid, Item item,
                          @RequestParam String otherTag){
        if(otherTag!=""){
            Set<Tag> tags = item.getTags();
            itemService.saveTag(new Tag(otherTag));
            Tag tag = itemService.getLastTag();
            tags.add(tag);
            item.setTags(tags);
        }
        Collection collection = collectionService.get(id);
        item.setCollection(collection);
        itemService.update(item);
        return "redirect:/profile/"+uid+"/collections/"+ id + "/items";
    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @GetMapping("/profile/{uid}/collections/{id}/edit_item/{item_id}")
    public String editItem(@PathVariable("id") int id,@PathVariable("item_id") int itemId, @PathVariable("uid") Long uid,Model model){
        Collection collection = collectionService.get(id);
        Item item = itemService.get(itemId);
        model.addAttribute("collection", collection);
        model.addAttribute("item", item);
        List<Tag> listTags = itemService.listTags();
        model.addAttribute("listTags", listTags);
        model.addAttribute("other", "");
        return "edit_item";
    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @PostMapping("/profile/{uid}/collections/{id}/save_item/{item_id}")
    public String saveItem(@PathVariable("id") int id, @PathVariable("uid") Long uid,@PathVariable("item_id") int itemId,
                           Item item,@RequestParam String otherTag){
        if(otherTag!=""){
            Set<Tag> tags = item.getTags();
            itemService.saveTag(new Tag(otherTag));
            Tag tag = itemService.getLastTag();
            tags.add(tag);
            item.setTags(tags);
        }
        item.setId(itemId);
        item.setCollection(collectionService.get(id));
        itemService.update(item);
        return "redirect:/profile/"+uid+"/collections/"+ id + "/items";
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
        return "redirect:/profile/"+uid+"/collections/"+ id + "/items";
    }

}
