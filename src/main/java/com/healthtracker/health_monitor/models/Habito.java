package com.healthtracker.health_monitor.models;



import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "habitos")
@Data
public class Habito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    private LocalDateTime creadoEn;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Otros atributos (frecuencia, duración, etc.)
}
