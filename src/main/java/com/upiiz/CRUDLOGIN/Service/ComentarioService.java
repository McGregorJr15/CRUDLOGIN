package com.upiiz.CRUDLOGIN.Service;

import com.upiiz.CRUDLOGIN.Entities.Comentario;
import com.upiiz.CRUDLOGIN.Repositories.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    public List<Comentario> listarTodos() {
        return comentarioRepository.findAll();
    }

    public void guardarComentario(Comentario comentario) {
        comentarioRepository.save(comentario);
    }

    public Optional<Comentario> obtenerPorId(Integer id) {
        return comentarioRepository.findById(id);
    }

    public void eliminarComentario(Integer id) {
        comentarioRepository.deleteById(id);
    }
}
