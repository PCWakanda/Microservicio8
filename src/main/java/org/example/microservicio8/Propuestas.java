// src/main/java/org/example/microservicio8/Propuestas.java
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
    private boolean aceptada;
    private int presupuesto; // Nuevo campo

    public Propuestas() {}

    public Propuestas(Long id, String nombre, int presupuesto) {
        this.id = id;
        this.nombre = nombre;
        this.aceptada = false;
        this.presupuesto = presupuesto; // Asignar presupuesto
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isAceptada() {
        return aceptada;
    }

    public int getPresupuesto() {
        return presupuesto;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setAceptada(boolean aceptada) {
        this.aceptada = aceptada;
    }

    public void setPresupuesto(int presupuesto) {
        this.presupuesto = presupuesto;
    }

    @Override
    public String toString() {
        return nombre + " (Aceptada: " + aceptada + ", Presupuesto: " + presupuesto + ")";
    }
}