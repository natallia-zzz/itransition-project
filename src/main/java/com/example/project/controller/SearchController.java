package com.example.project.controller;

import com.example.project.entity.Item;
import com.example.project.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    ItemService itemService;

    @RequestMapping("/searchResults")
    public String viewHomePage(Model model, @Param("keyword") String keyword) {
        List<Item> listItems = itemService.searchItems(keyword);
        model.addAttribute("listItems", listItems);
        model.addAttribute("keyword", keyword);
        return "search_results";
    }

}
