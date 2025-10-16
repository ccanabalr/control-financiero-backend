package com.proyecto.aplicacion.controladores;

import com.proyecto.aplicacion.dtos.CrearTransaccionDTO;
import com.proyecto.aplicacion.dtos.TransaccionDTO;
import com.proyecto.aplicacion.dtos.ResumenMensualDTO;
import com.proyecto.dominio.modelo.Transaccion;
import com.proyecto.dominio.modelo.ResumenMensual;
import com.proyecto.dominio.puertos.entrada.ServicioGestionTransaccion;
import com.proyecto.dominio.puertos.salida.RepositorioResumenMensual;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/transacciones")
public class ControladorTransaccion {

    private final ServicioGestionTransaccion servicioGestionTransaccion;
    private final RepositorioResumenMensual repositorioResumenMensual;

    public ControladorTransaccion(
            ServicioGestionTransaccion servicioGestionTransaccion,
            RepositorioResumenMensual repositorioResumenMensual) {
        this.servicioGestionTransaccion = servicioGestionTransaccion;
        this.repositorioResumenMensual = repositorioResumenMensual;
    }

    /**
     * Crear una nueva transacción
     * POST /api/v1/transacciones
     */
    @PostMapping
    public ResponseEntity<TransaccionDTO> crear(
            @Valid @RequestBody CrearTransaccionDTO dto,
            @AuthenticationPrincipal String usuarioId) {

        log.info("Creando nueva transaccion para usuario: {}", usuarioId);

        // Mapear DTO a modelo de dominio
        Transaccion transaccion = Transaccion.builder()
                .usuarioId(usuarioId)
                .cuentaId(dto.getCuentaId())
                .categoriaId(dto.getCategoriaId())
                .tipo(dto.getTipo())
                .monto(dto.getMonto())
                .descripcion(dto.getDescripcion())
                .etiquetas(dto.getEtiquetas())
                .fechaTransaccion(dto.getFechaTransaccion() != null ? dto.getFechaTransaccion() : LocalDateTime.now())
                .fechaCreacion(LocalDateTime.now())
                .build();

        try {
            // Crear transacción a través del servicio
            Transaccion transaccionGuardada = servicioGestionTransaccion.crearTransaccion(transaccion);

            // Mapear a DTO para respuesta
            TransaccionDTO respuesta = mapearADTO(transaccionGuardada);

            log.info("Transaccion creada exitosamente con ID: {}", transaccionGuardada.getId());
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(respuesta);

        } catch (IllegalArgumentException e) {
            log.warn("Error de validacion al crear transaccion: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Obtener transacción por ID
     * GET /api/v1/transacciones/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransaccionDTO> obtenerPorId(
            @PathVariable String id,
            @AuthenticationPrincipal String usuarioId) {

        log.debug("Obteniendo transaccion con ID: {}", id);

        Optional<Transaccion> transaccion = servicioGestionTransaccion.obtenerTransaccion(id);

        if (transaccion.isEmpty()) {
            log.warn("Transaccion no encontrada con ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        // Verificar que la transacción pertenece al usuario autenticado
        if (!transaccion.get().getUsuarioId().equals(usuarioId)) {
            log.warn("Acceso denegado: usuario {} intentó acceder transaccion de usuario {}", 
                    usuarioId, transaccion.get().getUsuarioId());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(mapearADTO(transaccion.get()));
    }

    /**
     * Listar transacciones del usuario autenticado
     * GET /api/v1/transacciones
     */
    @GetMapping
    public ResponseEntity<List<TransaccionDTO>> listarDelUsuario(
            @AuthenticationPrincipal String usuarioId) {

        log.debug("Listando transacciones del usuario: {}", usuarioId);

        List<Transaccion> transacciones = servicioGestionTransaccion.listarTransaccionesUsuario(usuarioId);

        List<TransaccionDTO> respuesta = transacciones.stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    /**
     * Listar transacciones de una cuenta específica
     * GET /api/v1/transacciones/cuenta/{cuentaId}
     */
    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<TransaccionDTO>> listarPorCuenta(
            @PathVariable String cuentaId,
            @AuthenticationPrincipal String usuarioId) {

        log.debug("Listando transacciones de la cuenta: {}", cuentaId);

        List<Transaccion> transacciones = servicioGestionTransaccion.listarTransaccionesCuenta(cuentaId);

        // Filtrar solo las transacciones del usuario autenticado
        List<TransaccionDTO> respuesta = transacciones.stream()
                .filter(t -> t.getUsuarioId().equals(usuarioId))
                .map(this::mapearADTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    /**
     * Actualizar una transacción
     * PUT /api/v1/transacciones/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransaccionDTO> actualizar(
            @PathVariable String id,
            @Valid @RequestBody CrearTransaccionDTO dto,
            @AuthenticationPrincipal String usuarioId) {

        log.info("Actualizando transaccion con ID: {}", id);

        // Obtener transacción existente
        Optional<Transaccion> transaccionExistente = servicioGestionTransaccion.obtenerTransaccion(id);

        if (transaccionExistente.isEmpty()) {
            log.warn("Transaccion no encontrada con ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        // Verificar permisos
        if (!transaccionExistente.get().getUsuarioId().equals(usuarioId)) {
            log.warn("Acceso denegado al actualizar transaccion");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Mapear cambios
        Transaccion transaccionActualizada = Transaccion.builder()
                .usuarioId(usuarioId)
                .cuentaId(dto.getCuentaId())
                .categoriaId(dto.getCategoriaId())
                .tipo(dto.getTipo())
                .monto(dto.getMonto())
                .descripcion(dto.getDescripcion())
                .etiquetas(dto.getEtiquetas())
                .fechaTransaccion(dto.getFechaTransaccion())
                .build();

        try {
            Transaccion actualizada = servicioGestionTransaccion.actualizarTransaccion(id, transaccionActualizada);
            log.info("Transaccion actualizada exitosamente");
            return ResponseEntity.ok(mapearADTO(actualizada));

        } catch (IllegalArgumentException e) {
            log.warn("Error al actualizar transaccion: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Eliminar una transacción
     * DELETE /api/v1/transacciones/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable String id,
            @AuthenticationPrincipal String usuarioId) {

        log.info("Eliminando transaccion con ID: {}", id);

        // Obtener transacción existente
        Optional<Transaccion> transaccion = servicioGestionTransaccion.obtenerTransaccion(id);

        if (transaccion.isEmpty()) {
            log.warn("Transaccion no encontrada con ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        // Verificar permisos
        if (!transaccion.get().getUsuarioId().equals(usuarioId)) {
            log.warn("Acceso denegado al eliminar transaccion");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            servicioGestionTransaccion.eliminarTransaccion(id);
            log.info("Transaccion eliminada exitosamente");
            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {
            log.warn("Error al eliminar transaccion: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Obtener resumen mensual del mes actual
     * GET /api/v1/transacciones/resumen-mensual?cuentaId=xxx&anio=2025&mes=10
     */
    @GetMapping("/resumen-mensual")
    public ResponseEntity<ResumenMensualDTO> obtenerResumenMensual(
            @RequestParam String cuentaId,
            @RequestParam(required = false) Integer anio,
            @RequestParam(required = false) Integer mes,
            @AuthenticationPrincipal String usuarioId) {

        log.debug("Obteniendo resumen mensual para cuenta: {}, usuario: {}", cuentaId, usuarioId);

        // Usar mes y año actuales si no se proporcionan
        YearMonth ahora = YearMonth.now();
        if (anio == null) {
            anio = ahora.getYear();
        }
        if (mes == null) {
            mes = ahora.getMonthValue();
        }

        Optional<ResumenMensual> resumen = repositorioResumenMensual
                .obtenerResumenPorCuentaYPeriodo(cuentaId, anio, mes);

        if (resumen.isEmpty()) {
            log.debug("No existe resumen mensual para cuenta: {}, anio: {}, mes: {}", cuentaId, anio, mes);
            return ResponseEntity.notFound().build();
        }

        // Verificar que el resumen pertenece al usuario autenticado
        if (!resumen.get().getUsuarioId().equals(usuarioId)) {
            log.warn("Acceso denegado a resumen de otro usuario");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ResumenMensualDTO dto = mapearResumenADTO(resumen.get());
        return ResponseEntity.ok(dto);
    }

    /**
     * Obtener resumen mensual del mes actual (versión simplificada)
     * GET /api/v1/transacciones/resumen-actual?cuentaId=xxx
     */
    @GetMapping("/resumen-actual")
    public ResponseEntity<ResumenMensualDTO> obtenerResumenActual(
            @RequestParam String cuentaId,
            @AuthenticationPrincipal String usuarioId) {

        log.debug("Obteniendo resumen actual para cuenta: {}", cuentaId);

        YearMonth ahora = YearMonth.now();
        Integer anio = ahora.getYear();
        Integer mes = ahora.getMonthValue();

        Optional<ResumenMensual> resumen = repositorioResumenMensual
                .obtenerResumenPorCuentaYPeriodo(cuentaId, anio, mes);

        if (resumen.isEmpty()) {
            log.debug("No existe resumen para mes actual");
            return ResponseEntity.notFound().build();
        }

        if (!resumen.get().getUsuarioId().equals(usuarioId)) {
            log.warn("Acceso denegado");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(mapearResumenADTO(resumen.get()));
    }

    /**
     * Mapea un modelo Transaccion a un DTO
     */
    private TransaccionDTO mapearADTO(Transaccion transaccion) {
        return TransaccionDTO.builder()
                .id(transaccion.getId())
                .usuarioId(transaccion.getUsuarioId())
                .cuentaId(transaccion.getCuentaId())
                .categoriaId(transaccion.getCategoriaId())
                .tipo(transaccion.getTipo())
                .monto(transaccion.getMonto())
                .descripcion(transaccion.getDescripcion())
                .fechaTransaccion(transaccion.getFechaTransaccion())
                .fechaCreacion(transaccion.getFechaCreacion())
                .fechaActualizacion(transaccion.getFechaActualizacion())
                .etiquetas(transaccion.getEtiquetas())
                .referenciaExterna(transaccion.getReferenciaExterna())
                .build();
    }

    /**
     * Mapea un modelo ResumenMensual a un DTO
     */
    private ResumenMensualDTO mapearResumenADTO(ResumenMensual resumen) {
        return ResumenMensualDTO.builder()
                .id(resumen.getId())
                .usuarioId(resumen.getUsuarioId())
                .cuentaId(resumen.getCuentaId())
                .anio(resumen.getAnio())
                .mes(resumen.getMes())
                .saldoInicial(resumen.getSaldoInicial())
                .saldoFinal(resumen.getSaldoFinal())
                .totalIngresos(resumen.getTotalIngresos())
                .totalEgresos(resumen.getTotalEgresos())
                .fechaCreacion(resumen.getFechaCreacion())
                .fechaActualizacion(resumen.getFechaActualizacion())
                .build();
    }
}
