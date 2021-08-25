package com.example.project.repository;

import com.example.project.entity.Role;
import com.example.project.entity.Topic;
import com.example.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic,Integer> {
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public Topic findByName(String name);
}
