package com.example.project.repository;

import com.example.project.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query("SELECT r FROM Tag r WHERE r.name = ?1")
    public Tag findByName(String name);
}
