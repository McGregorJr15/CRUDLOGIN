package com.upiiz.CRUDLOGIN.Controllers;

import com.upiiz.CRUDLOGIN.Entities.Post;
import com.upiiz.CRUDLOGIN.Entities.Usuario;
import com.upiiz.CRUDLOGIN.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;


    @GetMapping("/listado")
    public String listarPosts(Model model, HttpSession session) {
        
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("usuario", usuarioLogueado);
        model.addAttribute("posts", postService.obtenerTodos());

        // Apunta al listado.html en la raíz de templates
        return "listado";
    }


    @GetMapping("/nuevo")
        public String mostrarFormularioNuevo(Model model, HttpSession session) {
            Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
            if (usuarioLogueado == null) return "redirect:/auth/login";

            model.addAttribute("usuario", usuarioLogueado);
            
            // Simplemente mandamos un Post vacío normal
            model.addAttribute("post", new Post()); 
            
            return "crear_post"; 
        }

// 2. Recibir los datos del formulario y guardarlos
    @PostMapping("/guardar")
    public String guardarPost(@ModelAttribute Post post, RedirectAttributes redirectAttributes, HttpSession session) {
        
        // Verificamos que alguien tenga sesión iniciada por seguridad
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/auth/login";
        }

        try {
            // ¡AQUÍ ESTÁ LA MAGIA! 
            // Ya NO usamos post.setAutor(...)
            // Dejamos que Spring Boot guarde el post exactamente con el autor_id que viene del formulario.
            
            postService.guardarPost(post);
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Publicación creada exitosamente!");
            
        } catch (Exception e) {
            // Si el usuario escribe un ID (como 2565) que NO existe en la tabla de usuarios, MySQL lo va a rechazar.
            // Atrapamos ese error para no mostrar una pantalla blanca fea.
            redirectAttributes.addFlashAttribute("mensajeError", "Error: El ID de autor ingresado no existe en la base de datos.");
            return "redirect:/posts/nuevo"; // Lo regresamos al formulario
        }
        
        return "redirect:/posts/listado";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Integer id, Model model, HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("usuario", usuarioLogueado);

        // Buscamos el post por su ID
        Post post = postService.obtenerPorId(id).orElse(null);
        if (post == null) {
            return "redirect:/posts/listado"; // Si ponen un ID que no existe, los regresamos
        }

        model.addAttribute("post", post);
        return "editar_post"; // Buscará el archivo editar_post.html
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarPost(@PathVariable("id") Integer id, @ModelAttribute Post postDetalles, RedirectAttributes redirectAttributes) {
        Post postExistente = postService.obtenerPorId(id).orElse(null);
        
        if (postExistente != null) {
            try {
                // SÓLO actualizamos el título y el contenido
                postExistente.setTitulo(postDetalles.getTitulo());
                postExistente.setContenido(postDetalles.getContenido());
                
                postService.guardarPost(postExistente);
                redirectAttributes.addFlashAttribute("mensajeExito", "¡Publicación actualizada correctamente!");
                
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("mensajeError", "Error al actualizar la publicación.");
                return "redirect:/posts/editar/" + id; 
            }
        }
        return "redirect:/posts/listado";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPost(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/auth/login";
        }
        
        postService.eliminarPost(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Publicación eliminada.");
        return "redirect:/posts/listado";
    }
}