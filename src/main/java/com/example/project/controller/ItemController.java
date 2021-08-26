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
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        model.addAttribute("other", "");
        return "new_item";
    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @PostMapping("/profile/{uid}/collections/{id}/add_item")
    public String addItem(@PathVariable("id") int id, @PathVariable("uid") Long uid, Item item,
                          @RequestParam String otherTag){
        if(otherTag!=""){
            List<String> tagStringList = Arrays.asList(otherTag.split("\\s*,\\s*"));
            Tag temporaryTag;
            Set<Tag> tags = item.getTags();
            for (String element : tagStringList) {
                temporaryTag = itemService.saveTag(element);
                tags.add(temporaryTag);
            }
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
        Set<Tag> tags = item.getTags();
        List<String> stringList = new ArrayList<>();
        for (Tag element : tags) {
            stringList.add(element.getName());
        }
        model.addAttribute("collection", collection);
        model.addAttribute("item", item);
        model.addAttribute("other", String.join(",", stringList));
        return "edit_item";
    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @PostMapping("/profile/{uid}/collections/{id}/save_item/{item_id}")
    public String saveItem(@PathVariable("id") int id, @PathVariable("uid") Long uid,@PathVariable("item_id") int itemId,
                           Item item,@RequestParam String otherTag){
        if(otherTag!=""){
            List<String> tagStringList = Arrays.asList(otherTag.split("\\s*,\\s*"));
            Tag temporaryTag;
            Set<Tag> tags = item.getTags();
            for (String element : tagStringList) {
                temporaryTag = itemService.saveTag(element);
                tags.add(temporaryTag);
            }
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

    @RequestMapping(value="/tagsAutocomplete")
    @ResponseBody
    public List<Tag> plantNamesAutocomplete(@RequestParam(value="term", required = false, defaultValue="") String term)  {
        List<Tag> suggestions = new ArrayList<Tag>();
        try {
            // only update when term is three characters.
            if (term.length() == 3) {
                suggestions = itemService.fetchTags(term);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return suggestions;

    }

}
