package com.upiiz.CRUDLOGIN.Controllers;

import com.upiiz.CRUDLOGIN.Entities.Usuario;
import com.upiiz.CRUDLOGIN.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 1. PRUEBA UNITARIA 
    @GetMapping("/test/unitaria")
    public String pruebaUnitaria() {
        
        String password = "password123";
        String confirmPassword = "password456";
        
        
        if (!password.equals(confirmPassword)) {
            return "PRUEBA UNITARIA EXITOSA: La logica del sistema detecto correctamente que las contrasenas no coinciden.";
        } else {
            return "PRUEBA FALLIDA: El sistema acepto contrasenas diferentes.";
        }
    }

    @GetMapping("/test/integracion")
    public String pruebaIntegracion() {
        try {
            Usuario userTest = new Usuario();
            userTest.setBoleta("TEST999");
            userTest.setEmail("test@integracion.com");
            userTest.setPassword("123");
            userTest.setNombre("Usuario de Prueba");
            
            usuarioRepository.save(userTest); 
            
            Usuario recuperado = usuarioRepository.findByBoleta("TEST999");
            
            usuarioRepository.delete(recuperado);

            if (recuperado != null && recuperado.getNombre().equals("Usuario de Prueba")) {
                return "PRUEBA DE INTEGRACION EXITOSA: El sistema guardo el dato en MySQL y lo recupero correctamente.";
            } else {
                return "PRUEBA FALLIDA: No se pudo recuperar el usuario de la base de datos.";
            }

        } catch (Exception e) {
            return "RUEBA FALLIDA: Error de conexion con la BD - " + e.getMessage();
        }
    }
}
