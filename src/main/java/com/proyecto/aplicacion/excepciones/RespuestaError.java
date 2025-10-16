package com.proyecto.aplicacion.excepciones;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespuestaError {

    private String mensaje;

    private String detalle;

    private Integer codigoEstado;

    private LocalDateTime timestamp;

    private String ruta;
}
