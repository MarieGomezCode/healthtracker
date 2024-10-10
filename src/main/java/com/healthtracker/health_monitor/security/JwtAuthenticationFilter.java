package com.healthtracker.health_monitor.security;

import java.io.IOException;

import com.healthtracker.health_monitor.services.UsuarioServicio;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Imprimir el encabezado de autorización
        System.out.println("Encabezado de autorización: " + authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(token);
            // Imprimir el token y el nombre de usuario extraído
            System.out.println("Token extraído: " + token);
            System.out.println("Nombre de usuario extraído: " + username);
        } else {
            System.out.println("Encabezado de autorización no válido o ausente.");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = usuarioServicio.loadUserByUsername(username);

            // Imprimir detalles del usuario
            System.out.println("Detalles del usuario: " + userDetails);

            if (jwtUtil.validateToken(token, userDetails)) {
                // Extraer el ID del usuario del token
                Long userId = jwtUtil.extractUserId(token);
                System.out.println("ID de usuario extraído: " + userId);

                // Configurar el contexto de seguridad
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                // Guardar el ID del usuario en el contexto (si necesitas acceder más tarde)
                request.setAttribute("userId", userId); // Guarda el ID en la solicitud
                System.out.println("Usuario autenticado: " + username);
            } else {
                System.out.println("Token no válido para el usuario: " + username);
            }
        } else {
            System.out.println("Nombre de usuario no encontrado o autenticación ya establecida.");
        }


        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
