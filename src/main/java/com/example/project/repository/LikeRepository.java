package com.example.project.repository;

import com.example.project.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    @Query("SELECT r FROM Like r WHERE r.item.id = ?1")
    public List<Like> findByItemId(Integer id);
    public List<Like> findByUserId(Long id);
    public Like findByItemIdAndUserId(Integer itemId, Long userId);
}
