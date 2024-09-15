package com.healthtracker.health_monitor.services;


import com.healthtracker.health_monitor.models.Habito;
import com.healthtracker.health_monitor.repositories.HabitoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitoServicio {

    @Autowired
    private HabitoRepositorio habitoRepositorio;

    public Habito guardarHabito(Habito habito) {
        return habitoRepositorio.save(habito);
    }

    public List<Habito> encontrarPorUsuarioId(Long usuarioId) {
        return habitoRepositorio.findByUsuarioId(usuarioId);
    }
}
