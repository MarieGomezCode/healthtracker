package com.healthtracker.health_monitor.controllers;

import com.healthtracker.health_monitor.dto.HabitoDTO;
import com.healthtracker.health_monitor.models.Habito;
import com.healthtracker.health_monitor.models.Usuario;
import com.healthtracker.health_monitor.security.JwtUtil;
import com.healthtracker.health_monitor.services.HabitoServicio;
import com.healthtracker.health_monitor.services.UsuarioServicio; // Asegúrate de tener este servicio
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habitos")
@CrossOrigin(origins = "http://localhost:4200")
public class HabitoControlador {

    @Autowired
    private HabitoServicio habitoServicio;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioServicio usuarioServicio; // Servicio para obtener el usuario

    @PostMapping("/crear")
    public ResponseEntity<Habito> crearHabito(@RequestBody HabitoDTO habitoDTO, HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                Long userId = jwtUtil.extractUserId(token); // Extraer el ID del usuario desde el token

                // Obtener el usuario usando el ID extraído del token
                Usuario usuario = usuarioServicio.obtenerPorId(userId);

                if (usuario == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Usuario no encontrado
                }

                // Crear y guardar el hábito
                Habito habito = new Habito();
                habito.setNombre(habitoDTO.getNombre());
                habito.setDescripcion(habitoDTO.getDescripcion());
                habito.setCreadoEn(habitoDTO.getCreadoEn());
                habito.setUsuario(usuario); // Asignar la referencia del usuario

                Habito habitoGuardado = habitoServicio.guardarHabito(habito);
                return ResponseEntity.ok(habitoGuardado);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Token no proporcionado
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Habito>> obtenerHabitosPorUsuarioId(@PathVariable Long usuarioId) {
        List<Habito> habitos = habitoServicio.encontrarPorUsuarioId(usuarioId);
        return ResponseEntity.ok(habitos);
    }
}
