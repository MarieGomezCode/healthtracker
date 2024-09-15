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
    // Define que este método manejará las solicitudes HTTP POST dirigidas a la ruta "/registro" (combinado con la ruta base "/api/usuarios", resultando en "/api/usuarios/registro").

    public ResponseEntity<String> registrarUsuario(@RequestBody UsuarioRegistroDTO usuarioDTO) {
        // Método público que maneja el registro de un nuevo usuario.
        // @RequestBody indica que el cuerpo de la solicitud HTTP será deserializado en una instancia de UsuarioRegistroDTO.

        Usuario usuario = new Usuario();
        // Crea una nueva instancia de la clase Usuario.

        usuario.setNombre(usuarioDTO.getNombre());
        // Establece el nombre del usuario con el valor proporcionado en el DTO recibido.

        usuario.setCorreoElectronico(usuarioDTO.getCorreoElectronico());
        // Establece el correo electrónico del usuario con el valor proporcionado en el DTO recibido.

        usuario.setContrasena(usuarioDTO.getContrasena());
        // Establece la contraseña del usuario con el valor proporcionado en el DTO recibido.
        // Nota: Es importante cifrar la contraseña antes de guardarla por motivos de seguridad.

        Usuario usuarioGuardado = usuarioServicio.guardarUsuario(usuario);
        // Llama al servicio de usuarios para guardar el nuevo usuario en la base de datos.
        // El método guardarUsuario devuelve la instancia del usuario guardado, incluyendo el ID autogenerado.

        return ResponseEntity.ok("Usuario registrado exitosamente con ID: " + usuarioGuardado.getId());
        // Devuelve una respuesta HTTP 200 (OK) con un mensaje de éxito que incluye el ID del usuario recién registrado.
    }

    @PostMapping("/login")
    // Define que este método manejará las solicitudes HTTP POST dirigidas a la ruta "/login" (combinado con la ruta base "/api/usuarios", resultando en "/api/usuarios/login").

    public ResponseEntity<String> login(@RequestBody UsuarioLoginDTO loginDTO) {
        // Método público que maneja el proceso de login de un usuario.
        // @RequestBody indica que el cuerpo de la solicitud HTTP será deserializado en una instancia de UsuarioLoginDTO.

        Optional<Usuario> usuario = usuarioServicio.encontrarPorCorreoElectronico(loginDTO.getCorreoElectronico());
        // Llama al servicio de usuarios para buscar un usuario en la base de datos por su correo electrónico.
        // El resultado es un Optional que puede contener el usuario encontrado o estar vacío si no existe.

        if (usuario.isPresent() && usuario.get().getContrasena().equals(loginDTO.getContrasena())) {
            // Verifica si el usuario existe y si la contraseña proporcionada coincide con la almacenada.
            // Nota: Es fundamental utilizar un mecanismo de autenticación seguro, como el cifrado de contraseñas y la validación adecuada.

            return ResponseEntity.ok("Login exitoso");
            // Si las credenciales son correctas, devuelve una respuesta HTTP 200 (OK) con un mensaje de éxito.
        }

        return ResponseEntity.status(401).body("Credenciales inválidas");
        // Si las credenciales no son válidas, devuelve una respuesta HTTP 401 (Unauthorized) con un mensaje de error.
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
