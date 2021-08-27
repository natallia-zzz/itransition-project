package com.example.project.controller;

import com.example.project.entity.Item;
import com.example.project.entity.Tag;
import com.example.project.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class SearchController {

    @Autowired
    ItemService itemService;

    @RequestMapping("/searchResults")
    public String viewSearchResults(Model model, @RequestParam(name = "keyword") String keyword, @RequestParam(name = "filter", required = false) List<Tag> filter) {
        System.out.println(filter);
        List<Item> listItems = itemService.searchItems(keyword);
        List<Tag> filterTags = itemService.getFilterTags(listItems);
        model.addAttribute("filterTags", filterTags);
        List<Item> filteredItemList = new ArrayList<>();
        if(filter!= null){
            for(Item item:listItems){
                Set<Tag> tags = new HashSet<>(filter);
                tags.retainAll(item.getTags());
                if(!tags.isEmpty()){
                    filteredItemList.add(item);
                }
            }
            model.addAttribute("listItems", filteredItemList);
        }
        else{model.addAttribute("listItems", listItems);}
        model.addAttribute("filter", filter);
        model.addAttribute("keyword", keyword);
        return "search_results";
    }
}
