package com.healthtracker.health_monitor.controllers;
// Declaración del paquete donde se encuentra el controlador de usuarios.
// Esto organiza las clases relacionadas con el control de usuarios dentro de la aplicación.

import com.healthtracker.health_monitor.dto.UsuarioLoginDTO;
// Importa la clase UsuarioLoginDTO, que es un Data Transfer Object para manejar las solicitudes de login.

import com.healthtracker.health_monitor.dto.UsuarioRegistroDTO;
// Importa la clase UsuarioRegistroDTO, que es un Data Transfer Object para manejar las solicitudes de registro.

import com.healthtracker.health_monitor.models.Usuario;
// Importa la clase Usuario, que es el modelo que representa a un usuario en la base de datos.

import com.healthtracker.health_monitor.services.UsuarioServicio;
// Importa el servicio UsuarioServicio, que contiene la lógica de negocio relacionada con los usuarios.

import org.springframework.beans.factory.annotation.Autowired;
// Importa la anotación @Autowired, utilizada para inyectar dependencias automáticamente.

import org.springframework.http.ResponseEntity;
// Importa la clase ResponseEntity, que representa una respuesta HTTP completa (código de estado y cuerpo).

import org.springframework.web.bind.annotation.*;
// Importa las anotaciones necesarias para definir controladores REST y mapear rutas.

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
// Importa la clase Optional, que se utiliza para manejar valores que pueden o no estar presentes.

@RestController
// Indica que esta clase es un controlador REST, lo que permite manejar solicitudes HTTP y devolver respuestas en formato JSON.

@RequestMapping("/api/usuarios")
// Define la ruta base para todas las solicitudes que maneja este controlador. En este caso, todas las rutas comenzarán con "/api/usuarios".

public class UsuarioControlador {
// Declaración de la clase pública UsuarioControlador, que gestionará las solicitudes relacionadas con los usuarios.

    @Autowired
    // Indica a Spring que inyecte automáticamente una instancia de UsuarioServicio en esta clase.

    private UsuarioServicio usuarioServicio;
    // Define una variable para almacenar el servicio de usuarios, que contiene la lógica de negocio.

    @PostMapping("/registro")
    public ResponseEntity<Map<String, String>> registrarUsuario(@RequestBody UsuarioRegistroDTO usuarioDTO) {
        Optional<Usuario> usuarioExistente = usuarioServicio.encontrarPorCorreoElectronico(usuarioDTO.getCorreoElectronico());

        Map<String, String> response = new HashMap<>();
        if (usuarioExistente.isPresent()) {
            // Si el correo ya está registrado, devolver un mensaje de error
            response.put("mensaje", "correo ya registrado");
            return ResponseEntity.status(400).body(response);  // Devuelve código 400 (Bad Request)
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setCorreoElectronico(usuarioDTO.getCorreoElectronico());
        usuario.setContrasena(usuarioDTO.getContrasena());

        Usuario usuarioGuardado = usuarioServicio.guardarUsuario(usuario);

        response.put("mensaje", "Usuario registrado exitosamente");
        response.put("id", String.valueOf(usuarioGuardado.getId()));
        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UsuarioLoginDTO loginDTO) {
        Optional<Usuario> usuario = usuarioServicio.encontrarPorCorreoElectronico(loginDTO.getCorreoElectronico());
        Map<String, String> response = new HashMap<>();

        if (usuario.isEmpty()) {
            // Si el correo no está registrado
            response.put("mensaje", "Correo no registrado");
            return ResponseEntity.status(404).body(response);
        }

        if (!usuario.get().getContrasena().equals(loginDTO.getContrasena())) {
            // Si la contraseña es incorrecta
            response.put("mensaje", "Credenciales inválidas");
            return ResponseEntity.status(401).body(response);
        }

        // Si el correo y la contraseña son correctos
        response.put("mensaje", "Login exitoso");
        return ResponseEntity.ok(response);
    }



    @GetMapping("/{id}")
    // Define que este método manejará las solicitudes HTTP GET dirigidas a la ruta "/{id}", donde {id} es una variable de ruta que representa el ID del usuario.
    // Combinado con la ruta base, la ruta completa sería "/api/usuarios/{id}".

    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        // Método público que maneja la obtención de un usuario por su ID.
        // @PathVariable indica que el valor de {id} en la ruta será pasado como parámetro al método.

        Optional<Usuario> usuario = usuarioServicio.encontrarPorId(id);
        // Llama al servicio de usuarios para buscar un usuario en la base de datos por su ID.
        // El resultado es un Optional que puede contener el usuario encontrado o estar vacío si no existe.

        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
        // Si el usuario está presente, devuelve una respuesta HTTP 200 (OK) con el usuario en el cuerpo.
        // Si el usuario no existe, devuelve una respuesta HTTP 404 (Not Found) sin contenido en el cuerpo.
    }
}
