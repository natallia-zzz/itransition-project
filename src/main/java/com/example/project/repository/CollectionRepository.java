package com.example.project.repository;

import com.example.project.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Integer>{
    @Query("SELECT r FROM Collection r WHERE r.owner.id = ?1")
    public List<Collection> findByOwnerId(Long id);

}
