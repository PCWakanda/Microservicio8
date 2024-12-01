package org.example.microservicio8;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import java.util.Random;

@Entity
@Table(name = "votantes")
public class Votante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Propuestas propuesta;

    public Votante() {}

    public Votante(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Propuestas getPropuesta() {
        return propuesta;
    }

    public void setPropuesta(Propuestas propuesta) {
        this.propuesta = propuesta;
    }

    public boolean votar() {
        return Math.random() < 0.5; // Simula el voto aleatorio
    }
}
