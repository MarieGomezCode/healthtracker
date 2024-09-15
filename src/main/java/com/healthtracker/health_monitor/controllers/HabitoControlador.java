package com.healthtracker.health_monitor.controllers;


import com.healthtracker.health_monitor.models.Habito;
import com.healthtracker.health_monitor.services.HabitoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habitos")
public class HabitoControlador {

    @Autowired
    private HabitoServicio habitoServicio;

    @PostMapping("/crear")
    public ResponseEntity<Habito> crearHabito(@RequestBody Habito habito) {
        Habito habitoGuardado = habitoServicio.guardarHabito(habito);
        return ResponseEntity.ok(habitoGuardado);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Habito>> obtenerHabitosPorUsuarioId(@PathVariable Long usuarioId) {
        List<Habito> habitos = habitoServicio.encontrarPorUsuarioId(usuarioId);
        return ResponseEntity.ok(habitos);
    }
}
