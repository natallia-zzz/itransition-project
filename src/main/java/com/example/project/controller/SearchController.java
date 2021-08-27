package com.example.project.controller;

import com.example.project.entity.Item;
import com.example.project.entity.Tag;
import com.example.project.entity.Filter;
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
    public String viewSearchResults(Model model, Filter filter) {
        List<Item> listItems = itemService.searchItems(filter.getSearchString());
        List<String> filterTags = itemService.getFilterTagNames(listItems);
        model.addAttribute("filterTags", filterTags);
        List<Item> filteredItemList = new ArrayList<>();
        if(!filter.getFilterList().isEmpty()){
            for(Item item:listItems){
                Set<String> tags = new HashSet<>(filter.getFilterList());
                tags.retainAll(item.getTagNames());
                if(!tags.isEmpty()){
                    filteredItemList.add(item);
                }
            }
            model.addAttribute("listItems", filteredItemList);
        }
        else{model.addAttribute("listItems", listItems);}
        model.addAttribute("filter", filter);
        return "search_results";
    }
}
