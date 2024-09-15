package com.healthtracker.health_monitor.dto;



import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HabitoDTO {
    private String nombre;
    private String descripcion;
    private LocalDateTime creadoEn;
    private Long usuarioId;
}
