package com.healthtracker.health_monitor.services;


import com.healthtracker.health_monitor.models.Usuario;
import com.healthtracker.health_monitor.repositories.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;  // Asegúrate de que el nombre sea consistente

    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }

    public Optional<Usuario> encontrarPorCorreoElectronico(String correoElectronico) {
        return usuarioRepositorio.findByCorreoElectronico(correoElectronico);
    }

    public Optional<Usuario> encontrarPorId(Long id) {
        return usuarioRepositorio.findById(id);
    }
    public Usuario obtenerPorId(Long id) {
        return usuarioRepositorio.findById(id).orElse(null); // Devuelve null si no encuentra el usuario
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByCorreoElectronico(username)  // Aquí corregimos a 'usuarioRepositorio'
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + username));

        return new org.springframework.security.core.userdetails.User(
                usuario.getCorreoElectronico(),
                usuario.getContrasena(),
                new ArrayList<>()  // Añadir roles y autoridades si es necesario
        );
    }
}
