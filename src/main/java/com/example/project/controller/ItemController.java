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

    @GetMapping("/collections/{id}/additem")
    public String addItem(@PathVariable("id") int id, Model model){
        Collection collection = collectionService.get(id);
        model.addAttribute("item", new Item());
        model.addAttribute("collection", collection);
        return "new_item";
    }
}
