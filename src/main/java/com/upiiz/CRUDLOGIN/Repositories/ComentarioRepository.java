package com.upiiz.CRUDLOGIN.Repositories;

import com.upiiz.CRUDLOGIN.Entities.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {
}
