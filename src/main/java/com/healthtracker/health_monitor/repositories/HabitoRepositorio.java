package com.healthtracker.health_monitor.repositories;




import com.healthtracker.health_monitor.models.Habito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitoRepositorio extends JpaRepository<Habito, Long> {
    List<Habito> findByUsuarioId(Long usuarioId);
}
