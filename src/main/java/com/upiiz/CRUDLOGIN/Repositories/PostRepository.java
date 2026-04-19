package com.upiiz.CRUDLOGIN.Repositories;

import com.upiiz.CRUDLOGIN.Entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
    
}