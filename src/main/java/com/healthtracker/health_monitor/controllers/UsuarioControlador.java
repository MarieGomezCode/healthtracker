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
    // Define que este método manejará las solicitudes HTTP POST dirigidas a la ruta "/registro" (combinado con la ruta base "/api/usuarios", resultando en "/api/usuarios/registro").


    public ResponseEntity<Map<String, String>> registrarUsuario(@RequestBody UsuarioRegistroDTO usuarioDTO)
    {
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
        // Crear la respuesta en formato JSON
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Usuario registrado exitosamente");
        response.put("id", String.valueOf(usuarioGuardado.getId()));

        // Devolver la respuesta en formato JSON
        return ResponseEntity.ok(response);
        // Devuelve una respuesta HTTP 200 (OK) con un mensaje de éxito que incluye el ID del usuario recién registrado.
    }

    @PostMapping("/login")
// Este método maneja las solicitudes HTTP POST dirigidas a la ruta "/login".
// La ruta completa será "/api/usuarios/login", dado que la clase tendrá un prefijo "/api/usuarios".

    public ResponseEntity<Map<String, String>> login(@RequestBody UsuarioLoginDTO loginDTO) {
        // El método recibe un objeto `UsuarioLoginDTO` que contiene los datos enviados en el cuerpo de la solicitud (correo y contraseña).
        // Se utilizará un `ResponseEntity` con un `Map` para devolver la respuesta en formato JSON.

        Optional<Usuario> usuario = usuarioServicio.encontrarPorCorreoElectronico(loginDTO.getCorreoElectronico());
        // Llama al servicio de usuarios para buscar en la base de datos un usuario por su correo electrónico.
        // El resultado se envuelve en un `Optional`, lo que significa que el usuario puede existir o no.

        if (usuario.isPresent() && usuario.get().getContrasena().equals(loginDTO.getContrasena())) {
            // Verifica si el usuario existe (`isPresent()`) y si la contraseña proporcionada coincide con la contraseña almacenada en la base de datos.
            // NOTA: Para un sistema en producción, es importante usar contraseñas cifradas y mecanismos de autenticación seguros (como BCrypt).

            Map<String, String> response = new HashMap<>();
            // Se crea un `HashMap` para almacenar los datos que serán devueltos como respuesta en formato JSON.

            response.put("mensaje", "Login exitoso");
            // Se añade una clave "mensaje" al mapa con el valor "Login exitoso", que será parte de la respuesta JSON.

            return ResponseEntity.ok(response);
            // Devuelve un `ResponseEntity` con el mapa (convertido automáticamente a JSON) y un código de estado HTTP 200 (OK).
        }

        Map<String, String> errorResponse = new HashMap<>();
        // Si las credenciales no son correctas, se crea otro mapa para la respuesta de error.

        errorResponse.put("mensaje", "Credenciales inválidas");
        // Se añade un mensaje de error al mapa indicando que las credenciales son inválidas.

        return ResponseEntity.status(401).body(errorResponse);
        // Devuelve un `ResponseEntity` con el mapa de error y un código de estado HTTP 401 (Unauthorized).
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
