package com.example.project.controller;

import com.example.project.entity.Collection;
import com.example.project.entity.Item;
import com.example.project.service.CollectionService;
import com.example.project.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ItemController {

    @Autowired
    CollectionService collectionService;

    @Autowired
    ItemService itemService;

    @GetMapping("/collections/{id}/items")
    public String getCollectionItems(@PathVariable("id") int id, Model model){
        Collection collection = collectionService.get(id);
        List<Item> items = itemService.getByCollectionId(id);
        model.addAttribute("items", items);
        model.addAttribute("collection", collection);
        return "collection_items";
    }

    @GetMapping("/collections/{id}/new_item")
    public String newItem(@PathVariable("id") int id, Model model){
        Collection collection = collectionService.get(id);
        model.addAttribute("item", new Item());
        model.addAttribute("collection", collection);
        return "new_item";
    }

    @PostMapping("/collections/{id}/add_item")
    public String addItem(@PathVariable("id") int id, Item item){
        Collection collection = collectionService.get(id);
        item.setCollection(collection);
        itemService.update(item);
        return "redirect:/collections/"+ id + "/items";
    }

    @GetMapping("/collections/{id}/edit_item/{item_id}")
    public String editItem(@PathVariable("id") int id,@PathVariable("item_id") int itemId, Model model){
        Collection collection = collectionService.get(id);
        Item item = itemService.get(itemId);
        model.addAttribute("collection", collection);
        model.addAttribute("item", item);
        return "edit_item";
    }

    @PostMapping("/collections/{id}/save_item")
    public String saveItem(@PathVariable("id") int id, Item item){
        item.setCollection(collectionService.get(id));
        itemService.update(item);
        return "redirect:/collections/"+ id + "/items";
    }

    @GetMapping("/collections/{id}/delete_item/{item_id}")
    public String deleteItemConfirmation(@PathVariable("id") int id,@PathVariable("item_id") int itemId, Model model){
        Collection collection = collectionService.get(id);
        Item item = itemService.get(itemId);
        model.addAttribute("collection", collection);
        model.addAttribute("item", item);
        return "delete_item";
    }

    @PostMapping("/collections/{id}/delete_item")
    public String deleteItem(@PathVariable("id") int id, Item item){
        itemService.delete(item);
        return "redirect:/collections/"+ id + "/items";
    }

}
