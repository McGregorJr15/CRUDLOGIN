package com.upiiz.CRUDLOGIN.Service;

import com.upiiz.CRUDLOGIN.Entities.Post;
import com.upiiz.CRUDLOGIN.Repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> obtenerTodos() {
        return postRepository.findAll();
    }

    public Optional<Post> obtenerPorId(Integer id) {
        return postRepository.findById(id);
    }

    public Post guardarPost(Post post) {
        return postRepository.save(post);
    }

    public void eliminarPost(Integer id) {
        postRepository.deleteById(id);
    }
}