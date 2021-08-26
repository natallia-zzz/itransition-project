package com.example.project.repository;

import com.example.project.entity.Item;
import com.example.project.entity.Tag;
import com.example.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query("SELECT r FROM Tag r WHERE r.name = ?1")
    public Tag findByName(String name);
    public List<Tag> findByNameStartingWith(String searchTerm);
}
