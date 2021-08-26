package com.example.project.service;

import com.example.project.entity.Collection;
import com.example.project.entity.Item;
import com.example.project.entity.Tag;
import com.example.project.repository.ItemRepository;
import com.example.project.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ItemService {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    TagRepository tagRepository;

    public List<Item> listAll() {
        return itemRepository.findAll();
    }

    public List<Item> getByCollectionId(int id){ return itemRepository.findByCollectionId(id);}

    public void update(Item item){itemRepository.save(item);}

    public Item get(int id){ return itemRepository.getById(id);}

    public void delete(int id){ itemRepository.deleteById(id);}

    public List<Item> findLatest() {
        List<Item> items =this.listAll();
        int size=items.size();
        if(size<5) return items;
        else return items.subList(size-Math.min(size,5), size);}

    public List<Item> listItemsByTagId(int id){
        List<Item> items=new ArrayList<>();
        List<Item> allItems=itemRepository.findAll();
        for(Item item:allItems)
        {
            Set<Tag> tags = item.getTags();
            if(item.getTags().contains(tagRepository.getById(id))) items.add(item);
        }
        return items;
    }

}
