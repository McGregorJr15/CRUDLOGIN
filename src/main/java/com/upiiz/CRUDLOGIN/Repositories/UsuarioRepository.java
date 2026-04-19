package com.upiiz.CRUDLOGIN.Repositories;

import com.upiiz.CRUDLOGIN.Entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Este método mágico busca en la base de datos a un usuario por su email
    Usuario findByEmail(String email);
    Usuario findByBoleta(String boleta);
}