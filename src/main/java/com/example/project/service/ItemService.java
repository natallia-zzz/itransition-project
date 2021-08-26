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

import java.util.List;

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

    public List<Tag> listTags(){ return tagRepository.findAll();}

    public void processTagRequest(List<String> inputStringList){

    }

    public Tag saveTag(String name){
        if(tagRepository.findByName(name)==null) {
            Tag tag = new Tag(name);
            tagRepository.save(tag);
            return getLastTag();
        }
        return tagRepository.findByName(name);
    }

    public Tag getLastTag(){
        return tagRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).get(0);
    }

    public List<Tag> fetchTags(String searchTerm) throws Exception {
        return tagRepository.findByNameStartingWith(searchTerm);
    }
}
