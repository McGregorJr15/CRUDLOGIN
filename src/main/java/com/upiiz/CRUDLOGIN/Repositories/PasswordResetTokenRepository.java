package com.upiiz.CRUDLOGIN.Repositories;

import com.upiiz.CRUDLOGIN.Entities.PasswordResetToken;
import com.upiiz.CRUDLOGIN.Entities.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUsuario(Usuario usuario);
}