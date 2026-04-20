package com.upiiz.CRUDLOGIN.Controllers;

import com.upiiz.CRUDLOGIN.Entities.Usuario;
import com.upiiz.CRUDLOGIN.Repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private AuthController authController;

    @Test
    public void testRegisterUser_PasswordsDoNotMatch() {
        
        String boleta = "2020111111";
        String email = "test@test.com";
        String password = "password123";
        String confirmPassword = "password456";

        
        String result = authController.registerUser(boleta, email, password, confirmPassword, redirectAttributes);


        assertEquals("redirect:/auth/register", result); 
        
        verify(redirectAttributes).addFlashAttribute("mensajeError", "Las contraseñas no coinciden.");
        
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}
