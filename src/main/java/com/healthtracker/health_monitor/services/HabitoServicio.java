package com.healthtracker.health_monitor.services;


import com.healthtracker.health_monitor.models.Habito;
import com.healthtracker.health_monitor.models.Usuario;
import com.healthtracker.health_monitor.repositories.HabitoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HabitoServicio {

    @Autowired
    private HabitoRepositorio habitoRepositorio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    public Habito guardarHabito(Habito habito, Long usuarioId) {
        Optional<Usuario> usuario = usuarioServicio.encontrarPorId(usuarioId);
        if (usuario.isPresent()) {
            habito.setUsuario(usuario.get());
            return habitoRepositorio.save(habito);
        } else {
            throw new RuntimeException("Usuario no encontrado con id: " + usuarioId);
        }
    }

    public List<Habito> encontrarPorUsuarioId(Long usuarioId) {
        return habitoRepositorio.findByUsuarioId(usuarioId);
    }
}
