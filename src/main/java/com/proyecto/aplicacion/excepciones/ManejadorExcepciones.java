package com.proyecto.aplicacion.excepciones;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ManejadorExcepciones {

    /**
     * Maneja excepciones de validación de DTOs
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> manejarValidacion(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        log.warn("Error de validacion en la solicitud");

        Map<String, Object> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String nombreCampo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(nombreCampo, mensaje);
        });

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("estado", HttpStatus.BAD_REQUEST.value());
        respuesta.put("mensaje", "Error de validacion");
        respuesta.put("errores", errores);
        respuesta.put("ruta", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<RespuestaError> manejarArgumentoIlegal(
            IllegalArgumentException ex,
            WebRequest request) {

        log.warn("Argumento ilegal: {}", ex.getMessage());

        RespuestaError respuesta = RespuestaError.builder()
                .mensaje("Solicitud inválida")
                .detalle(ex.getMessage())
                .codigoEstado(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .ruta(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones genéricas
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<RespuestaError> manejarExcepcionGeneral(
            Exception ex,
            WebRequest request) {

        log.error("Error interno del servidor", ex);

        RespuestaError respuesta = RespuestaError.builder()
                .mensaje("Error interno del servidor")
                .detalle(ex.getMessage())
                .codigoEstado(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .ruta(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maneja excepciones de recurso no encontrado
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<RespuestaError> manejarNoEncontrado(
            IllegalArgumentException ex,
            WebRequest request) {

        log.warn("Recurso no encontrado: {}", ex.getMessage());

        RespuestaError respuesta = RespuestaError.builder()
                .mensaje("Recurso no encontrado")
                .detalle(ex.getMessage())
                .codigoEstado(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .ruta(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
    }
}
