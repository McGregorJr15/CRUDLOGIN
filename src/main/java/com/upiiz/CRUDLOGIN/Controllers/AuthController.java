package com.upiiz.CRUDLOGIN.Controllers;

import com.upiiz.CRUDLOGIN.Entities.PasswordResetToken;
import com.upiiz.CRUDLOGIN.Entities.Usuario;
import com.upiiz.CRUDLOGIN.Repositories.PasswordResetTokenRepository;
import com.upiiz.CRUDLOGIN.Repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/forgotpassword")
    public String forgotpassword(){
        return "forgotpassword";
    }

    @GetMapping("/recoverpassword")
    public String recoverpassword(@RequestParam("token") String token, Model model){
        model.addAttribute("token", token);
        return "recoverpassword";
    }

    // --- PROCESAR LOGIN ---
    @PostMapping("/processLogin")
    public String processLogin(@RequestParam("boleta") String boleta,
                               @RequestParam("password") String password,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        Usuario usuario = usuarioRepository.findByBoleta(boleta);

        if (usuario == null || !usuario.getPassword().equals(password)) {
            redirectAttributes.addFlashAttribute("mensajeError", "Boleta o contraseña incorrectos.");
            return "redirect:/auth/login";
        }

        session.setAttribute("usuarioLogueado", usuario);
        return "redirect:/inicio"; 
    }

    // --- PROCESAR REGISTRO ---
    @PostMapping("/registerUser")
    public String registerUser(@RequestParam("boleta") String boleta,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam("confirmPassword") String confirmPassword,
                               RedirectAttributes redirectAttributes) {

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("mensajeError", "Las contraseñas no coinciden.");
            return "redirect:/auth/register";
        }

        if (usuarioRepository.findByBoleta(boleta) != null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ya existe un usuario registrado con esta boleta.");
            return "redirect:/auth/register";
        }

        if (usuarioRepository.findByEmail(email) != null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Este correo electrónico ya está en uso.");
            return "redirect:/auth/register";
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setBoleta(boleta);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(password);
        nuevoUsuario.setNombre("Alumno " + boleta); 
        
        usuarioRepository.save(nuevoUsuario);

        redirectAttributes.addFlashAttribute("mensajeExito", "Registro exitoso. ¡Ya puedes iniciar sesión con tu boleta!");
        return "redirect:/auth/login";
    }

    // --- MANDAR CORREO DE RECUPERACIÓN ---
    @PostMapping("/resetpassword")
    public String processForgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "No encontramos ninguna cuenta con ese correo electrónico.");
            return "redirect:/auth/forgotpassword";
        }

        String nuevoTokenStr = UUID.randomUUID().toString();
        PasswordResetToken miToken = tokenRepository.findByUsuario(usuario);
        
        if (miToken != null) {
            miToken.setToken(nuevoTokenStr);
            miToken.setFechaExpiracion(LocalDateTime.now().plusMinutes(30));
        } else {
            miToken = new PasswordResetToken(nuevoTokenStr, usuario);
        }
        
        tokenRepository.save(miToken);

        String resetUrl = "http://localhost:8080/auth/recoverpassword?token=" + nuevoTokenStr;

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(email);
        mensaje.setSubject("Recuperación de Contraseña - Sistema X");
        mensaje.setText("Para recuperar tu contraseña, haz clic en el siguiente enlace:\n\n" + resetUrl);
        
        try {
            mailSender.send(mensaje);
            redirectAttributes.addFlashAttribute("mensajeExito", "Revisa tu bandeja de entrada. Te hemos enviado un enlace para recuperar tu contraseña.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error al enviar el correo. Por favor intenta más tarde.");
        }

        return "redirect:/auth/forgotpassword";
    }

    // --- GUARDAR LA NUEVA CONTRASEÑA ---
    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token,
                               @RequestParam("password") String password,
                               @RequestParam("confirmPassword") String confirmPassword,
                               RedirectAttributes redirectAttributes) {

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("mensajeError", "Las contraseñas no coinciden.");
            return "redirect:/auth/recoverpassword?token=" + token;
        }

        PasswordResetToken resetToken = tokenRepository.findByToken(token);

        if (resetToken == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Enlace de recuperación inválido.");
            return "redirect:/auth/login";
        }

        if (resetToken.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("mensajeError", "El enlace ha expirado. Por favor, solicita uno nuevo.");
            return "redirect:/auth/forgotpassword";
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setPassword(password); 
        usuarioRepository.save(usuario);

        tokenRepository.delete(resetToken);

        redirectAttributes.addFlashAttribute("mensajeExito", "Contraseña actualizada exitosamente. Ya puedes iniciar sesión.");
        return "redirect:/auth/login";
    }
}