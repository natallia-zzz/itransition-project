package com.example.project.service;

import com.example.project.entity.Collection;
import com.example.project.entity.Comment;
import com.example.project.entity.Item;
import com.example.project.entity.Tag;
import com.example.project.repository.CommentRepository;
import com.example.project.repository.ItemRepository;
import com.example.project.repository.LikeRepository;
import com.example.project.repository.TagRepository;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ItemService {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    TagRepository tagRepository;
    @Autowired
    LikeRepository likeRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public List<Item> listAll() {
        return itemRepository.findAll();
    }

    public List<Item> getByCollectionId(int id){ return itemRepository.findByCollectionId(id);}

    public void update(Item item){itemRepository.save(item);}

    public Item get(int id){ return itemRepository.getById(id);}

    public void delete(int id){

        itemRepository.deleteById(id);}

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
            if(item.getTags().contains(tagRepository.getById(id))) items.add(item);
        }
        return items;
    }

    public List<Tag> fetchTags(String searchTerm) throws Exception {
        return tagRepository.findByNameStartingWith(searchTerm);
    }

    @Transactional
    public List<Item> searchItems(String searchString){
        if(searchString!= null) {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            SearchSession searchSession = Search.session(entityManager);
            SearchResult<Item> result = searchSession.search(Item.class)
                    .where(f -> f.match()
                            .fields("name", "collection.description")
                            .matching(searchString))
                    .fetchAll();
            long totalHitCount = result.total().hitCount();
            List<Item> results = result.hits();
            return results;
        }
        return listAll();
    }

//    @Transactional
//    public List<Item> searchItems(String searchString){
//        if(searchString!= null) {
//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//            SearchSession searchSession = Search.session(entityManager);
//            SearchResult<Comment> result = searchSession.search(Comment.class)
//                    .where(f -> f.match()
//                            .fields("item.name", "item.collection.description", "content")
//                            .matching(searchString))
//                    .fetchAll();
//            List<Comment> comments = result.hits();
//            List<Item> results = new ArrayList<>();
//            for(Comment comment :comments){
//                results.add(comment.getItem());
//            }
//            return results;
//        }
//        return listAll();
//    }

    public List<String> getFilterTagNames(List<Item> items){
        Set<Tag> filterTags = new HashSet<>();
        for(Item item:items){
            Set<Tag> tags = item.getTags();
            filterTags.addAll(tags);
        }
        List<String> filterTagNames = new ArrayList<>();
        for(Tag tag:filterTags){
            filterTagNames.add(tag.getName());
        }
        return filterTagNames;
    }
}
