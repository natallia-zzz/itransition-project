package com.example.project.repository;

import com.example.project.entity.Collection;
import com.example.project.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query("SELECT r FROM Item r WHERE r.collection.id = ?1")
    public List<Item> findByCollectionId(int id);
}
