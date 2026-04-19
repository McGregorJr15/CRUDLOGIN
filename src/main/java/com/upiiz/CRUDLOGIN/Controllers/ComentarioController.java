package com.upiiz.CRUDLOGIN.Controllers;

import com.upiiz.CRUDLOGIN.Entities.Comentario;
import com.upiiz.CRUDLOGIN.Entities.Usuario;
import com.upiiz.CRUDLOGIN.Service.ComentarioService;
import com.upiiz.CRUDLOGIN.Service.PostService; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    // ¡NUEVO SERVICIO AGREGADO AQUÍ!
    @Autowired
    private PostService postService;

    // Mostrar el listado de comentarios
    @GetMapping("/listado")
    public String listarComentarios(Model model, HttpSession session) {
        // Verificar sesión de usuario
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("usuario", usuarioLogueado);
        model.addAttribute("comentarios", comentarioService.listarTodos());
        
        return "listado_comentarios";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model, HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null) return "redirect:/auth/login";

        model.addAttribute("usuario", usuarioLogueado);
        model.addAttribute("comentario", new Comentario()); // Mandamos un objeto vacío
        
        // ¡NUEVA LÍNEA AQUÍ! Mandamos la lista de posts a la pantalla
        model.addAttribute("posts", postService.obtenerTodos());
        
        return "crear_comentario"; // Buscará el archivo HTML
    }

    @PostMapping("/guardar")
    public String guardarComentario(@ModelAttribute Comentario comentario, RedirectAttributes redirectAttributes) {
        try {
            comentarioService.guardarComentario(comentario);
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Comentario guardado correctamente!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al guardar el comentario.");
        }
        return "redirect:/comentarios/listado";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Integer id, Model model, HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null) return "redirect:/auth/login";

        Comentario comentario = comentarioService.obtenerPorId(id).orElse(null);
        if (comentario == null) return "redirect:/comentarios/listado";

        model.addAttribute("usuario", usuarioLogueado);
        model.addAttribute("comentario", comentario);
        return "editar_comentario"; 
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarComentario(@PathVariable("id") Integer id, @ModelAttribute Comentario comentarioDetalles, RedirectAttributes redirectAttributes) {
        Comentario comentarioExistente = comentarioService.obtenerPorId(id).orElse(null);
        
        if (comentarioExistente != null) {
            // Permitimos actualizar contenido, y tal vez el autor si se equivocaron al escribirlo
            comentarioExistente.setContenido(comentarioDetalles.getContenido());
            comentarioExistente.setAutor(comentarioDetalles.getAutor());
            
            // NO actualizamos el post_id para que no lo muevan a otro post por error
            
            comentarioService.guardarComentario(comentarioExistente);
            redirectAttributes.addFlashAttribute("mensajeExito", "Comentario actualizado.");
        }
        return "redirect:/comentarios/listado";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarComentario(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/auth/login";
        
        comentarioService.eliminarComentario(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Comentario eliminado.");
        return "redirect:/comentarios/listado";
    }
}
