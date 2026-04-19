package com.upiiz.CRUDLOGIN.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Integer id;

    // El email debe ser único porque lo usaremos para iniciar sesión y recuperar contraseña
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 20)
    private String boleta;

    // Constructor vacío exigido por JPA
    public Usuario() {
    }

    // Constructor con parámetros
    public Usuario(String email, String password, String nombre, String boleta) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.boleta = boleta;
    }

    // --- Getters y Setters ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getBoleta() {
        return boleta;
    }
    public void setBoleta(String boleta) {
        this.boleta = boleta;
    }
}
