package com.example.project.service;

import com.example.project.entity.Like;
import com.example.project.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;
    public List<Like> likeList(Integer id){return likeRepository.findByItemId(id);}
    public List<Like> likeListByUser(Long id){return likeRepository.findByUserId(id);}
    public Like likeListByItemAndUser(Integer itemId, Long userId){return likeRepository.findByItemIdAndUserId(itemId,userId);}
    public void save(Like like) {likeRepository.save(like);}
    public void delete(Like like){likeRepository.delete(like);}

}
