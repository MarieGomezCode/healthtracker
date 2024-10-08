package com.healthtracker.health_monitor.controllers;
import com.healthtracker.health_monitor.dto.UsuarioLoginDTO;
import com.healthtracker.health_monitor.dto.UsuarioRegistroDTO;
import com.healthtracker.health_monitor.models.Habito;
import com.healthtracker.health_monitor.models.Usuario;
// Importa la clase Usuario, que es el modelo que representa a un usuario en la base de datos.


import com.healthtracker.health_monitor.security.JwtUtil;
import com.healthtracker.health_monitor.services.HabitoServicio;
import com.healthtracker.health_monitor.services.UsuarioServicio;
// Importa el servicio UsuarioServicio, que contiene la lógica de negocio relacionada con los usuarios.
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;




@RestController
@RequestMapping("/api/usuarios")
public class UsuarioControlador {

    @Autowired

    private UsuarioServicio usuarioServicio;
    // Inyecta el servicio UsuarioServicio en el controlador para poder utilizar sus métodos.
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private HabitoServicio habitoServicio;


    @PostMapping("/registro")
    public ResponseEntity<Map<String, String>> registrarUsuario(@RequestBody UsuarioRegistroDTO usuarioDTO) {
        Map<String, String> response = new HashMap<>();

        // Elimina espacios en los datos
        usuarioDTO.setNombre(usuarioDTO.getNombre().trim());
        usuarioDTO.setCorreoElectronico(usuarioDTO.getCorreoElectronico().trim());
        usuarioDTO.setContrasena(usuarioDTO.getContrasena().trim());

        // Verifica que los campos no estén vacíos
        if (usuarioDTO.getNombre().isBlank() || usuarioDTO.getCorreoElectronico().isBlank() || usuarioDTO.getContrasena().isBlank()) {
            response.put("mensaje", "Complete todos los campos");
            return ResponseEntity.status(400).body(response);
        }

        // Verifica que el correo no esté registrado
        Optional<Usuario> usuarioExistente = usuarioServicio.encontrarPorCorreoElectronico(usuarioDTO.getCorreoElectronico());
        if (usuarioExistente.isPresent()) {
            response.put("mensaje", "correo ya registrado");
            return ResponseEntity.status(400).body(response);
        }

        // Valida formato de contraseña
        String contrasenaRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$";
        if (!usuarioDTO.getContrasena().matches(contrasenaRegex)) {
            response.put("mensaje", "La contraseña no cumple con los requisitos");
            return ResponseEntity.status(400).body(response);
        }

        // Valida que el nombre sea solo uno y sin espacios
        if (!usuarioDTO.getNombre().matches("^[a-zA-Z]+$")) {
            response.put("mensaje", "Ingrese solo un nombre sin espacios");
            return ResponseEntity.status(400).body(response);
        }



        // Guarda el usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setCorreoElectronico(usuarioDTO.getCorreoElectronico());
        usuario.setContrasena(usuarioDTO.getContrasena());

        Usuario usuarioGuardado = usuarioServicio.guardarUsuario(usuario);

        response.put("mensaje", "Usuario registrado exitosamente");
        response.put("id", String.valueOf(usuarioGuardado.getId()));
        return ResponseEntity.ok(response);
    }
    //Aqui  no paso la prueba de validar que no hallan espacios en los datos  entonces se cambio el orden de las operacion se movio el que quitaEspacios a antes de validar datos


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UsuarioLoginDTO loginDTO) {
        Map<String, String> response = new HashMap<>();

        if (loginDTO.getCorreoElectronico().isBlank() || loginDTO.getContrasena().isBlank()) {
            response.put("mensaje", "Por favor ingrese los datos");
            return ResponseEntity.status(400).body(response);
        }

        Optional<Usuario> usuario = usuarioServicio.encontrarPorCorreoElectronico(loginDTO.getCorreoElectronico());

        if (usuario.isEmpty()) {
            response.put("mensaje", "Correo no registrado");
            return ResponseEntity.status(404).body(response);
        }

        if (!usuario.get().getContrasena().equals(loginDTO.getContrasena())) {
            response.put("mensaje", "Contraseña incorrecta");
            return ResponseEntity.status(401).body(response);
        }

        // Si el correo y la contraseña son correctos
        // Generar el token
        Long userId = usuario.get().getId(); // Obtener el ID del usuario
        String token = jwtUtil.generateToken(usuario.get().getCorreoElectronico(), userId); // Pasar el ID del usuario
        response.put("mensaje", "Login exitoso");
        response.put("token", token);
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
