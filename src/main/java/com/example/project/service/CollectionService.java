package com.example.project.service;

import com.example.project.entity.Collection;
import com.example.project.entity.User;
import com.example.project.repository.CollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class CollectionService {
    @Autowired
    CollectionRepository collectionRepo;
    public List<Collection> listAll() {
        return collectionRepo.findAll();
    }
    public List<Collection> collectionList(Long id){return collectionRepo.findByOwnerId(id);}
    public List<Collection> collectionListTopic(Long id){return collectionRepo.findByTopicId(id);}
    public void delete(Integer id){collectionRepo.deleteById(id);}
    public Collection get(Integer id){return collectionRepo.getById(id);}
    public void update(Collection collection){collectionRepo.save(collection);}
    public List<Collection> getTop5(){
        List<Collection> collections=this.listAll();
        int size=collections.size();
        collections.sort(new Comparator<Collection>() {
            @Override
            public int compare(Collection c1, Collection c2) {
                int size1=c1.getItems().size();
                int size2=c2.getItems().size();
                return size1 > size2 ? -1 : size1 == size2 ? 0 : 1;
            }
        });

        if(size<5) return collections.subList(0,size);
        else return collections.subList(0,5);
    }
}
