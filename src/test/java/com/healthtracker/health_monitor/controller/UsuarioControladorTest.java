package com.healthtracker.health_monitor.controller;

import com.healthtracker.health_monitor.controllers.UsuarioControlador;
import com.healthtracker.health_monitor.dto.UsuarioRegistroDTO;
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

class UsuarioControladorTest {

    @Mock
    private UsuarioServicio usuarioServicio;  // Mock del servicio

    @InjectMocks
    private UsuarioControlador usuarioControlador;  // Clase a probar

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Inicializa los mocks
    }

    @Test
    void testRegistrarUsuario_CamposVacios() {
        System.out.println("Ejecutando test: testRegistrarUsuario_CamposVacios");

        // Configuración del DTO con campos vacíos
        UsuarioRegistroDTO usuarioDTO = new UsuarioRegistroDTO();
        usuarioDTO.setNombre("");
        usuarioDTO.setCorreoElectronico("");
        usuarioDTO.setContrasena("");

        // Ejecutar el método
        ResponseEntity<Map<String, String>> response = usuarioControlador.registrarUsuario(usuarioDTO);

        // Verificaciones
        assertEquals(400, response.getStatusCodeValue());  // Verifica el código de estado 400
        assertEquals("Complete todos los campos", response.getBody().get("mensaje"));  // Verifica el mensaje de error

        System.out.println("Verificado: El sistema responde con un error cuando los campos están vacíos.");
    }

    @Test
    void testRegistrarUsuario_CorreoYaRegistrado() {
        System.out.println("Ejecutando test: testRegistrarUsuario_CorreoYaRegistrado");

        // Configuración del DTO
        UsuarioRegistroDTO usuarioDTO = new UsuarioRegistroDTO();
        usuarioDTO.setNombre("Angela");
        usuarioDTO.setCorreoElectronico("ella@gmail.com");
        usuarioDTO.setContrasena("Passw0rd!");

        // Simular que el correo ya está registrado
        when(usuarioServicio.encontrarPorCorreoElectronico("ella@gmail.com"))
                .thenReturn(Optional.of(new Usuario()));

        // Ejecutar el método
        ResponseEntity<Map<String, String>> response = usuarioControlador.registrarUsuario(usuarioDTO);

        // Verificaciones
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("correo ya registrado", response.getBody().get("mensaje"));

        System.out.println("Verificado: El sistema detecta que el correo ya está registrado.");
    }

    @Test
    public void testRegistrarUsuario_ContrasenaNoCumpleRequisitos() {
        System.out.println("Ejecutando test: testRegistrarUsuario_ContrasenaNoCumpleRequisitos");

        UsuarioRegistroDTO usuarioDTO = new UsuarioRegistroDTO();
        usuarioDTO.setNombre("Angela");
        usuarioDTO.setCorreoElectronico("nueva@gmail.com");
        usuarioDTO.setContrasena("123");

        ResponseEntity<Map<String, String>> response = usuarioControlador.registrarUsuario(usuarioDTO);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("La contraseña no cumple con los requisitos", response.getBody().get("mensaje"));

        System.out.println("Verificado: El sistema rechaza una contraseña que no cumple con los requisitos.");
    }

    @Test
    public void testRegistrarUsuario_EliminaEspaciosEnDatos() {
        System.out.println("Ejecutando test: testRegistrarUsuario_EliminaEspaciosEnDatos");

        UsuarioRegistroDTO usuarioDTO = new UsuarioRegistroDTO();
        usuarioDTO.setNombre(" Angela ");
        usuarioDTO.setCorreoElectronico(" angela@gmail.com ");
        usuarioDTO.setContrasena(" Contrasena1! ");

        // Simular que el usuario guardado tiene un ID (1 en este caso)
        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setId(1L);  // Simular que el ID es 1

        // Configurar el mock para que el método guardarUsuario retorne el usuario simulado
        when(usuarioServicio.guardarUsuario(any(Usuario.class))).thenReturn(usuarioGuardado);

        // Ejecutar el método
        ResponseEntity<Map<String, String>> response = usuarioControlador.registrarUsuario(usuarioDTO);

        assertEquals(200, response.getStatusCodeValue());
        // Verifica que los datos han sido guardados sin espacios
        assertEquals("Angela", usuarioDTO.getNombre());
        assertEquals("angela@gmail.com", usuarioDTO.getCorreoElectronico());
        assertEquals("Contrasena1!", usuarioDTO.getContrasena());

        // Verifica que el usuario fue guardado correctamente
        assertEquals("Usuario registrado exitosamente", response.getBody().get("mensaje"));
        assertEquals("1", response.getBody().get("id"));

        System.out.println("Verificado: El sistema elimina los espacios en los datos de entrada correctamente.");
    }
            //Esta prueba no paso entonces se cambio el orden de las operacion se movio el que quita los espacios antes de validar datos

    @Test
    public void testRegistrarUsuario_NombreConDosPalabras() {
        System.out.println("Ejecutando test: testRegistrarUsuario_NombreConDosPalabras");

        UsuarioRegistroDTO usuarioDTO = new UsuarioRegistroDTO();
        usuarioDTO.setNombre("Angela Maria");
        usuarioDTO.setCorreoElectronico("angela@gmail.com");
        usuarioDTO.setContrasena("Contrasena1!");

        ResponseEntity<Map<String, String>> response = usuarioControlador.registrarUsuario(usuarioDTO);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Ingrese solo un nombre sin espacios", response.getBody().get("mensaje"));

        System.out.println("Verificado: El sistema rechaza nombres con más de una palabra.");
    }

}
