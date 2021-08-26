package com.example.project.service;

import com.example.project.entity.Collection;
import com.example.project.entity.Item;
import com.example.project.entity.Tag;
import com.example.project.repository.ItemRepository;
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
import java.util.List;

@Service
public class ItemService {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

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

    @Transactional
    public List<Item> searchItems(String searchString){
        if(searchString!= null){
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            SearchSession searchSession = Search.session( entityManager );
            SearchResult<Item> result = searchSession.search(Item.class )
                    .where( f -> f.match()
                            .fields( "name", "collection.description" )
                            .matching( searchString ) )
                    .fetchAll();
            long totalHitCount = result.total().hitCount();
            List<Item> results = result.hits();
            return results;
        }
        return listAll().subList(0,10);
    }
}
