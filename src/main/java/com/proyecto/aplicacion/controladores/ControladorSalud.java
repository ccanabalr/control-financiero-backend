package com.proyecto.aplicacion.controladores;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class ControladorSalud {

    @GetMapping
    public Map<String, String> verificarSalud() {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("estado", "OK");
        respuesta.put("servicio", "Control Financiero Backend");
        respuesta.put("version", "1.0.0");
        return respuesta;
    }
}
