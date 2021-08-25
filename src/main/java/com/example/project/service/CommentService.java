package com.example.project.service;

import com.example.project.entity.Collection;
import com.example.project.entity.Comment;
import com.example.project.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    public List<Comment> commentList(Integer id){return commentRepository.findByItemId(id);}
    public void save(Comment comment) {commentRepository.save(comment);}

}
