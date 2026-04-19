package com.upiiz.CRUDLOGIN.Controllers;

import com.upiiz.CRUDLOGIN.Entities.Usuario;
import com.upiiz.CRUDLOGIN.Service.PostService;
import com.upiiz.CRUDLOGIN.Service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private PostService postService;

    @Autowired
    private ComentarioService comentarioService;

    @GetMapping("/inicio")
    public String mostrarDashboard(Model model, HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null) return "redirect:/auth/login";

        model.addAttribute("usuario", usuarioLogueado);

        // OBTENEMOS LOS CONTEOS REALES
        int totalPosts = postService.obtenerTodos().size();
        int totalComentarios = comentarioService.listarTodos().size();

        // MANDAMOS LOS DATOS A LA VISTA
        model.addAttribute("totalPosts", totalPosts);
        model.addAttribute("totalComentarios", totalComentarios);

        return "inicio";
    }
}