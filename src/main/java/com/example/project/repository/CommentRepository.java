package com.example.project.repository;

import com.example.project.entity.Collection;
import com.example.project.entity.Comment;
import com.example.project.entity.Item;
import com.example.project.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT r FROM Comment r WHERE r.item.id = ?1")
    public List<Comment> findByItemId(Integer id);
}
