package com.proyecto.infraestructura.seguridad;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import jakarta.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@Configuration
public class ConfiguracionFirebase {

    @PostConstruct
    public void inicializarFirebase() {
        if (FirebaseApp.getApps().isEmpty()) {
            try {
                Resource resource = new ClassPathResource("serviceAccountKey.json");
                
                if (resource.exists()) {
                    FirebaseOptions opciones = FirebaseOptions.builder()
                            .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                            .build();

                    FirebaseApp.initializeApp(opciones);
                    log.info("Firebase inicializado correctamente");
                } else {
                    log.warn("serviceAccountKey.json no encontrado. Firebase no será inicializado. Esto es normal en desarrollo.");
                }
            } catch (IOException e) {
                log.warn("Error al inicializar Firebase: {}. Continuando sin Firebase.", e.getMessage());
            }
        }
    }
}
