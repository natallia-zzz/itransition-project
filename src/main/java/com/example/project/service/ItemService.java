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

    public void update(Item item){itemRepository.save(item);}

    public Item get(int id){ return itemRepository.getById(id);}

    public void delete(int id){ itemRepository.deleteById(id);}

    public List<Item> findLatest() {
        List<Item> items =this.listAll();
        int size=items.size();
        try{
            return items.subList(size-Math.min(size,5), size);}
        catch(Exception e) {
            return null;
    }
    }
}
