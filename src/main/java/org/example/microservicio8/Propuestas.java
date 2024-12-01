package org.example.microservicio8;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "propuestas")
public class Propuestas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private boolean aceptada; // Nuevo campo

    public Propuestas() {}

    public Propuestas(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.aceptada = false; // Por defecto, no aceptada
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isAceptada() {
        return aceptada;
    }

    public void setAceptada(boolean aceptada) {
        this.aceptada = aceptada;
    }

    @Override
    public String toString() {
        return nombre + " (Aceptada: " + aceptada + ")";
    }
}
