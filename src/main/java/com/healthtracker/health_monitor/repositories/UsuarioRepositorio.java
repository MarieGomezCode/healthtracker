    package com.healthtracker.health_monitor.repositories;




    import com.healthtracker.health_monitor.models.Usuario;
    import org.springframework.data.jpa.repository.JpaRepository;

    import java.util.Optional;

    public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
        Optional<Usuario> findByCorreoElectronico(String correoElectronico);

    }
