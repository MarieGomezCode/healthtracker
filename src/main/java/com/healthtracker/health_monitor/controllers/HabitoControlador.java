package com.healthtracker.health_monitor.controllers;
// Declaración del paquete donde se encuentra el controlador, que organiza las clases relacionadas con el control de la lógica en la aplicación.

import com.healthtracker.health_monitor.dto.HabitoDTO;
// Importa la clase HabitoDTO, que se utiliza para transferir datos entre el cliente y el servidor.

import com.healthtracker.health_monitor.models.Habito;
// Importa la clase modelo Habito, que representa una entidad en la base de datos.

import com.healthtracker.health_monitor.services.HabitoServicio;
// Importa el servicio HabitoServicio, que maneja la lógica de negocio relacionada con los hábitos.

import org.springframework.beans.factory.annotation.Autowired;
// Importa la anotación @Autowired, que se utiliza para inyectar dependencias automáticamente.

import org.springframework.http.ResponseEntity;
// Importa la clase ResponseEntity, que representa una respuesta HTTP completa (con cuerpo y estado) enviada desde un controlador.

import org.springframework.web.bind.annotation.*;
// Importa las anotaciones necesarias para definir un controlador REST y los mappings de las rutas.

import java.util.List;
// Importa la clase List, que se utiliza para manejar colecciones de objetos, en este caso, listas de hábitos.

@RestController
// Define que esta clase es un controlador REST, lo que permite manejar solicitudes HTTP y devolver datos en formato JSON.

@RequestMapping("/api/habitos")
// Define la ruta base de todas las peticiones que este controlador manejará, en este caso, "/api/habitos".

public class HabitoControlador {
// Declaración de la clase pública HabitoControlador, que gestiona las solicitudes HTTP relacionadas con los hábitos.

    @Autowired
    // Indica que Spring debe inyectar automáticamente una instancia de HabitoServicio en esta clase.

    private HabitoServicio habitoServicio;
    // Define una variable para almacenar el servicio de hábitos, que contiene la lógica de negocio.

    @PostMapping("/crear")
    // Define que este método responderá a solicitudes HTTP POST en la ruta "/crear".

    public ResponseEntity<Habito> crearHabito(@RequestBody HabitoDTO habitoDTO) {
        // Método que maneja la creación de un nuevo hábito. Toma un objeto HabitoDTO del cuerpo de la solicitud y devuelve una respuesta con el hábito creado o un error.
        try {
            // Inicia un bloque try para manejar posibles excepciones.

            Habito habito = new Habito();
            // Crea una nueva instancia de la clase Habito.

            habito.setNombre(habitoDTO.getNombre());
            // Establece el nombre del hábito usando el valor del DTO recibido.

            habito.setDescripcion(habitoDTO.getDescripcion());
            // Establece la descripción del hábito con el valor del DTO.

            habito.setCreadoEn(habitoDTO.getCreadoEn());
            // Establece la fecha de creación del hábito desde el DTO.

            Habito habitoGuardado = habitoServicio.guardarHabito(habito, habitoDTO.getUsuarioId());
            // Llama al servicio para guardar el nuevo hábito en la base de datos, vinculándolo al usuario correspondiente, y guarda el resultado en habitoGuardado.

            return ResponseEntity.ok(habitoGuardado);
            // Si todo sale bien, devuelve una respuesta HTTP 200 (OK) con el hábito guardado en el cuerpo de la respuesta.
        } catch (RuntimeException e) {
            // Captura cualquier excepción que ocurra durante la ejecución.

            return ResponseEntity.badRequest().body(null);
            // Si ocurre un error, devuelve una respuesta HTTP 400 (Bad Request) sin contenido en el cuerpo.
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    // Define que este método responderá a solicitudes HTTP GET en la ruta "/usuario/{usuarioId}", donde {usuarioId} es un parámetro de la ruta.

    public ResponseEntity<List<Habito>> obtenerHabitosPorUsuarioId(@PathVariable Long usuarioId) {
        // Método que maneja la obtención de todos los hábitos de un usuario específico. Toma el ID del usuario como parámetro de la ruta y devuelve una lista de hábitos.
        List<Habito> habitos = habitoServicio.encontrarPorUsuarioId(usuarioId);
        // Llama al servicio para encontrar todos los hábitos asociados con el ID del usuario.

        return ResponseEntity.ok(habitos);
        // Devuelve una respuesta HTTP 200 (OK) con la lista de hábitos en el cuerpo de la respuesta.
    }
}
