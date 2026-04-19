package com.upiiz.CRUDLOGIN.Entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer comentario_id;

    // ID de la publicación a la que pertenece
    @Column(name = "post_id", nullable = false)
    private Integer post_id;

    // Aquí es texto normal (VARCHAR)
    @Column(nullable = false, length = 100)
    private String autor;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fecha_creacion;

    // --- Constructor vacío ---
    public Comentario() {}

    // --- Getters y Setters ---
    public Integer getComentario_id() { return comentario_id; }
    public void setComentario_id(Integer comentario_id) { this.comentario_id = comentario_id; }

    public Integer getPost_id() { return post_id; }
    public void setPost_id(Integer post_id) { this.post_id = post_id; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public LocalDateTime getFecha_creacion() { return fecha_creacion; }
    public void setFecha_creacion(LocalDateTime fecha_creacion) { this.fecha_creacion = fecha_creacion; }
}

