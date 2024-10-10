package com.healthtracker.health_monitor;

import com.healthtracker.health_monitor.security.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HealthMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthMonitorApplication.class, args);
	}

	@Bean
	public JwtUtil jwtUtil() {
		return new JwtUtil(); // Asegúrate de tener una instancia de JwtUtil
	}
}
