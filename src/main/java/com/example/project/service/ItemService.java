package com.example.project.service;

import com.example.project.entity.Collection;
import com.example.project.entity.Item;
import com.example.project.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    ItemRepository itemRepository;

    public List<Item> listAll() {
        return itemRepository.findAll();
    }

    public List<Item> getByCollectionId(int id){ return itemRepository.findByCollectionId(id);}
}
