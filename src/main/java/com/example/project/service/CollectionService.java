package com.example.project.service;

import com.example.project.entity.Collection;
import com.example.project.entity.User;
import com.example.project.repository.CollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionService {
    @Autowired
    CollectionRepository collectionRepo;
    public List<Collection> listAll() {
        return collectionRepo.findAll();
    }
    public void delete(Integer id){collectionRepo.deleteById(id);}
    public Collection get(Integer id){return collectionRepo.getById(id);}
    public void update(Collection collection){collectionRepo.save(collection);}
}
