package com.healthtracker.health_monitor.services;


import com.healthtracker.health_monitor.models.Usuario;
import com.healthtracker.health_monitor.repositories.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }

    public Optional<Usuario> encontrarPorCorreoElectronico(String correoElectronico) {
        return usuarioRepositorio.findByCorreoElectronico(correoElectronico);
    }

    public Optional<Usuario> encontrarPorId(Long id) {
        return usuarioRepositorio.findById(id);
    }
}
