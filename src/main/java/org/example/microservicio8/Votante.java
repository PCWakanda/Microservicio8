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
    private transient Random random = new Random();

    @ManyToOne
    private Propuestas propuesta;

    public Votante() {
    }

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
        return random.nextBoolean();
    }
}