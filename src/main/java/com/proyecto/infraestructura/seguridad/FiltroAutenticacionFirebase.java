package com.proyecto.infraestructura.seguridad;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Component
public class FiltroAutenticacionFirebase extends OncePerRequestFilter {

    private static final String ENCABEZADO_AUTORIZACION = "Authorization";
    private static final String PREFIJO_BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extraerToken(request);

        if (token != null) {
            try {
                FirebaseToken tokenDecodificado = FirebaseAuth.getInstance().verifyIdToken(token);
                String uid = tokenDecodificado.getUid();

                UsernamePasswordAuthenticationToken autenticacion =
                        new UsernamePasswordAuthenticationToken(uid, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(autenticacion);

                log.debug("Usuario autenticado: {}", uid);
            } catch (FirebaseAuthException e) {
                log.warn("Error al verificar token Firebase: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extraerToken(HttpServletRequest request) {
        String encabezado = request.getHeader(ENCABEZADO_AUTORIZACION);
        if (encabezado != null && encabezado.startsWith(PREFIJO_BEARER)) {
            return encabezado.substring(PREFIJO_BEARER.length());
        }
        return null;
    }
}
