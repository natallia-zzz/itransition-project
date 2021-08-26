package com.example.project.service;

import com.example.project.entity.Tag;
import com.example.project.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public List<Tag> tagList(){return tagRepository.findAll();}
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
