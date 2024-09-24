package com.healthtracker.health_monitor.controller;

import com.healthtracker.health_monitor.controllers.UsuarioControlador;
import com.healthtracker.health_monitor.dto.UsuarioLoginDTO; // Asegúrate de tener este DTO para el inicio de sesión
import com.healthtracker.health_monitor.models.Usuario;
import com.healthtracker.health_monitor.services.UsuarioServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ControladorTestLogin {

    @Mock
    private UsuarioServicio usuarioServicio;  // Mock del servicio

    @InjectMocks
    private UsuarioControlador usuarioControlador;  // Clase a probar

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Inicializa los mocks
    }

    @Test
    void testIniciarSesion_CamposVacios() {
        System.out.println("Ejecutando test de inicio de sesión: testIniciarSesion_CamposVacios");

        UsuarioLoginDTO usuarioDTO = new UsuarioLoginDTO();
        usuarioDTO.setCorreoElectronico("");
        usuarioDTO.setContrasena("");

        ResponseEntity<Map<String, String>> response = usuarioControlador.login(usuarioDTO);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Por favor ingrese los datos", response.getBody().get("mensaje"));
        System.out.println("Verificado: El sistema responde con un error cuando los campos están vacíos.");
    }

    @Test
    void testIniciarSesion_CredencialesInvalidas() {
        System.out.println("Ejecutando test de inicio de sesión: testIniciarSesion_CredencialesInvalidas");

        UsuarioLoginDTO usuarioDTO = new UsuarioLoginDTO();
        usuarioDTO.setCorreoElectronico("usuario@gmail.com");
        usuarioDTO.setContrasena("contrasenaIncorrecta");

        // Crear un usuario simulado con la contraseña correcta
        Usuario usuario = new Usuario();
        usuario.setCorreoElectronico("usuario@gmail.com");
        usuario.setContrasena("contrasenaCorrecta"); // Establece la contraseña correcta

        // Simular que el usuario existe
        when(usuarioServicio.encontrarPorCorreoElectronico("usuario@gmail.com"))
                .thenReturn(Optional.of(usuario));

        ResponseEntity<Map<String, String>> response = usuarioControlador.login(usuarioDTO);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Contraseña incorrecta", response.getBody().get("mensaje"));
        System.out.println("Verificado: El sistema responde con un error para credenciales inválidas.");
    }


    @Test
    void testIniciarSesion_UsuarioNoRegistrado() {
        System.out.println("Ejecutando test de inicio de sesión: testIniciarSesion_UsuarioNoRegistrado");

        UsuarioLoginDTO usuarioDTO = new UsuarioLoginDTO();
        usuarioDTO.setCorreoElectronico("noexistente@gmail.com");
        usuarioDTO.setContrasena("Passw0rd!");

        when(usuarioServicio.encontrarPorCorreoElectronico("noexistente@gmail.com"))
                .thenReturn(Optional.empty()); // Simular que el usuario no existe

        ResponseEntity<Map<String, String>> response = usuarioControlador.login(usuarioDTO);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Correo no registrado", response.getBody().get("mensaje"));
        System.out.println("Verificado: El sistema responde cuando el usuario no está registrado.");
    }

    @Test
    void testIniciarSesion_ContrasenaIncorrecta() {
        System.out.println("Ejecutando test de inicio de sesión: testIniciarSesion_ContrasenaIncorrecta");

        UsuarioLoginDTO usuarioDTO = new UsuarioLoginDTO();
        usuarioDTO.setCorreoElectronico("usuario@gmail.com");
        usuarioDTO.setContrasena("contrasenaIncorrecta");

        Usuario usuario = new Usuario();
        usuario.setCorreoElectronico("usuario@gmail.com");
        usuario.setContrasena("contrasenaCorrecta"); // Simular que esta es la contraseña correcta

        when(usuarioServicio.encontrarPorCorreoElectronico("usuario@gmail.com"))
                .thenReturn(Optional.of(usuario)); // Simular que el usuario existe

        ResponseEntity<Map<String, String>> response = usuarioControlador.login(usuarioDTO);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Contraseña incorrecta", response.getBody().get("mensaje"));
        System.out.println("Verificado: El sistema responde con un error si la contraseña es incorrecta.");
    }

    @Test
    void testIniciarSesion_SuccessfulLogin() {
        System.out.println("Ejecutando test de inicio de sesión: testIniciarSesion_SuccessfulLogin");

        UsuarioLoginDTO usuarioDTO = new UsuarioLoginDTO();
        usuarioDTO.setCorreoElectronico("usuario@gmail.com");
        usuarioDTO.setContrasena("contrasenaCorrecta");

        Usuario usuario = new Usuario();
        usuario.setCorreoElectronico("usuario@gmail.com");
        usuario.setContrasena("contrasenaCorrecta");

        when(usuarioServicio.encontrarPorCorreoElectronico("usuario@gmail.com"))
                .thenReturn(Optional.of(usuario)); // Simular que el usuario existe

        ResponseEntity<Map<String, String>> response = usuarioControlador.login(usuarioDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Login exitoso", response.getBody().get("mensaje"));
        System.out.println("Verificado: El sistema inicia sesión correctamente con credenciales válidas.");
    }
}
