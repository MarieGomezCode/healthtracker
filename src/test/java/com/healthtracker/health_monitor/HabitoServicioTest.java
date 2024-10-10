package com.healthtracker.health_monitor;

import com.healthtracker.health_monitor.models.Habito;
import com.healthtracker.health_monitor.repositories.HabitoRepositorio;
import com.healthtracker.health_monitor.services.HabitoServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HabitoServicioTest {

    @Mock
    private HabitoRepositorio habitoRepositorio;

    @InjectMocks
    private HabitoServicio habitoServicio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarHabito() {
        // Arrange
        Habito habito = new Habito();
        habito.setNombre("Ejercicio");
        habito.setDescripcion("Hacer ejercicio diariamente");

        when(habitoRepositorio.save(any(Habito.class))).thenReturn(habito);

        // Act
        Habito habitoGuardado = habitoServicio.guardarHabito(habito);

        // Assert
        assertNotNull(habitoGuardado);
        assertEquals("Ejercicio", habitoGuardado.getNombre());
        verify(habitoRepositorio, times(1)).save(habito);
    }

    @Test
    void testEncontrarPorUsuarioId() {
        // Arrange
        Long usuarioId = 1L;
        List<Habito> habitos = new ArrayList<>();
        habitos.add(new Habito());

        when(habitoRepositorio.findByUsuarioId(usuarioId)).thenReturn(habitos);

        // Act
        List<Habito> habitosEncontrados = habitoServicio.encontrarPorUsuarioId(usuarioId);

        // Assert
        assertNotNull(habitosEncontrados);
        assertFalse(habitosEncontrados.isEmpty());
        assertEquals(1, habitosEncontrados.size());
        verify(habitoRepositorio, times(1)).findByUsuarioId(usuarioId);
    }
}
