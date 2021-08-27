package com.example.project.service;

import com.example.project.entity.Collection;
import com.example.project.entity.Topic;
import com.example.project.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {
    @Autowired
    private TopicRepository topicRepository;

    public List<Topic> listAll() {
        return topicRepository.findAll();
    }
    public void delete(Integer id){topicRepository.deleteById(id);}
    public Topic get(Integer id){return topicRepository.getById(id);}
    public void update(Topic topic){topicRepository.save(topic);}
}
