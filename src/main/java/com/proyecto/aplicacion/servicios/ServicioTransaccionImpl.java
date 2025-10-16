package com.proyecto.aplicacion.servicios;

import com.proyecto.dominio.modelo.Transaccion;
import com.proyecto.dominio.modelo.ResumenMensual;
import com.proyecto.dominio.puertos.entrada.ServicioGestionTransaccion;
import com.proyecto.dominio.puertos.salida.RepositorioTransaccion;
import com.proyecto.dominio.puertos.salida.RepositorioResumenMensual;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ServicioTransaccionImpl implements ServicioGestionTransaccion {

    private final RepositorioTransaccion repositorioTransaccion;
    private final RepositorioResumenMensual repositorioResumenMensual;

    public ServicioTransaccionImpl(
            RepositorioTransaccion repositorioTransaccion,
            RepositorioResumenMensual repositorioResumenMensual) {
        this.repositorioTransaccion = repositorioTransaccion;
        this.repositorioResumenMensual = repositorioResumenMensual;
    }

    @Override
    public Transaccion crearTransaccion(Transaccion transaccion) {
        log.info("Creando transaccion para usuario: {}", transaccion.getUsuarioId());

        // Validaciones de negocio
        validarTransaccion(transaccion);

        // Establecer fechas si no están presentes
        if (transaccion.getFechaTransaccion() == null) {
            transaccion.setFechaTransaccion(LocalDateTime.now());
        }
        if (transaccion.getFechaCreacion() == null) {
            transaccion.setFechaCreacion(LocalDateTime.now());
        }

        // Guardar la transacción
        Transaccion transaccionGuardada = repositorioTransaccion.guardarTransaccion(transaccion);
        log.info("Transaccion creada con ID: {}", transaccionGuardada.getId());

        // Actualizar resumen mensual
        actualizarResumenMensual(transaccionGuardada);

        return transaccionGuardada;
    }

    @Override
    public Optional<Transaccion> obtenerTransaccion(String id) {
        log.debug("Obteniendo transaccion con ID: {}", id);
        return repositorioTransaccion.obtenerTransaccionPorId(id);
    }

    @Override
    public List<Transaccion> listarTransaccionesUsuario(String usuarioId) {
        log.debug("Listando transacciones del usuario: {}", usuarioId);
        return repositorioTransaccion.obtenerTransaccionesUsuario(usuarioId);
    }

    @Override
    public List<Transaccion> listarTransaccionesCuenta(String cuentaId) {
        log.debug("Listando transacciones de la cuenta: {}", cuentaId);
        return repositorioTransaccion.obtenerTransaccionesCuenta(cuentaId);
    }

    @Override
    public Transaccion actualizarTransaccion(String id, Transaccion transaccionActualizada) {
        log.info("Actualizando transaccion con ID: {}", id);

        Optional<Transaccion> transaccionExistente = repositorioTransaccion.obtenerTransaccionPorId(id);

        if (transaccionExistente.isEmpty()) {
            throw new IllegalArgumentException("Transaccion no encontrada con ID: " + id);
        }

        Transaccion transaccion = transaccionExistente.get();

        // Actualizar campos permitidos
        if (transaccionActualizada.getDescripcion() != null) {
            transaccion.setDescripcion(transaccionActualizada.getDescripcion());
        }
        if (transaccionActualizada.getMonto() != null) {
            transaccion.setMonto(transaccionActualizada.getMonto());
        }
        if (transaccionActualizada.getCategoriaId() != null) {
            transaccion.setCategoriaId(transaccionActualizada.getCategoriaId());
        }
        if (transaccionActualizada.getEtiquetas() != null) {
            transaccion.setEtiquetas(transaccionActualizada.getEtiquetas());
        }

        transaccion.setFechaActualizacion(LocalDateTime.now());

        repositorioTransaccion.actualizar(transaccion);
        log.info("Transaccion actualizada correctamente");

        return transaccion;
    }

    @Override
    public void eliminarTransaccion(String id) {
        log.info("Eliminando transaccion con ID: {}", id);

        Optional<Transaccion> transaccion = repositorioTransaccion.obtenerTransaccionPorId(id);

        if (transaccion.isEmpty()) {
            throw new IllegalArgumentException("Transaccion no encontrada con ID: " + id);
        }

        repositorioTransaccion.eliminarTransaccion(id);
        log.info("Transaccion eliminada correctamente");

        // Recalcular resumen mensual después de eliminar
        recalcularResumenesMensuales(transaccion.get().getUsuarioId());
    }

    @Override
    public void recalcularResumenesMensuales(String usuarioId) {
        log.info("Recalculando resumenes mensuales para usuario: {}", usuarioId);

        List<Transaccion> transacciones = repositorioTransaccion.obtenerTransaccionesUsuario(usuarioId);

        // Agrupar por cuenta y período
        for (Transaccion transaccion : transacciones) {
            LocalDateTime fecha = transaccion.getFechaTransaccion();
            Integer anio = fecha.getYear();
            Integer mes = fecha.getMonthValue();

            Optional<ResumenMensual> resumenOpt = repositorioResumenMensual
                    .obtenerResumenPorCuentaYPeriodo(transaccion.getCuentaId(), anio, mes);

            if (resumenOpt.isPresent()) {
                ResumenMensual resumen = resumenOpt.get();
                recalcularResumen(resumen, transaccion.getCuentaId(), anio, mes);
                repositorioResumenMensual.actualizar(resumen);
            }
        }

        log.info("Resumenes mensuales recalculados");
    }

    /**
     * Valida que la transacción cumpla con las reglas de negocio
     */
    private void validarTransaccion(Transaccion transaccion) {
        if (transaccion.getMonto() == null || transaccion.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Monto inválido: {}", transaccion.getMonto());
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }

        if (transaccion.getUsuarioId() == null || transaccion.getUsuarioId().isBlank()) {
            log.warn("Usuario ID vacío");
            throw new IllegalArgumentException("El usuario ID es requerido");
        }

        if (transaccion.getCuentaId() == null || transaccion.getCuentaId().isBlank()) {
            log.warn("Cuenta ID vacía");
            throw new IllegalArgumentException("El cuenta ID es requerido");
        }

        if (transaccion.getTipo() == null || transaccion.getTipo().isBlank()) {
            log.warn("Tipo de transaccion vacío");
            throw new IllegalArgumentException("El tipo de transaccion es requerido");
        }

        if (!transaccion.getTipo().matches("INGRESO|GASTO")) {
            log.warn("Tipo de transaccion inválido: {}", transaccion.getTipo());
            throw new IllegalArgumentException("El tipo debe ser INGRESO o GASTO");
        }

        log.debug("Transaccion validada correctamente");
    }

    /**
     * Actualiza el resumen mensual con los datos de la nueva transacción
     */
    private void actualizarResumenMensual(Transaccion transaccion) {
        LocalDateTime fecha = transaccion.getFechaTransaccion();
        Integer anio = fecha.getYear();
        Integer mes = fecha.getMonthValue();

        Optional<ResumenMensual> resumenOpt = repositorioResumenMensual
                .obtenerResumenPorCuentaYPeriodo(transaccion.getCuentaId(), anio, mes);

        ResumenMensual resumen;

        if (resumenOpt.isPresent()) {
            resumen = resumenOpt.get();
        } else {
            resumen = crearResumenMensual(transaccion, anio, mes);
        }

        // Actualizar totales según tipo
        if ("INGRESO".equals(transaccion.getTipo())) {
            BigDecimal nuevoTotal = resumen.getTotalIngresos().add(transaccion.getMonto());
            resumen.setTotalIngresos(nuevoTotal);
            resumen.setSaldoFinal(resumen.getSaldoInicial().add(nuevoTotal).subtract(resumen.getTotalEgresos()));
        } else if ("GASTO".equals(transaccion.getTipo())) {
            BigDecimal nuevoTotal = resumen.getTotalEgresos().add(transaccion.getMonto());
            resumen.setTotalEgresos(nuevoTotal);
            resumen.setSaldoFinal(resumen.getSaldoInicial().add(resumen.getTotalIngresos()).subtract(nuevoTotal));
        }

        resumen.setFechaActualizacion(LocalDateTime.now());

        repositorioResumenMensual.guardarResumen(resumen);
        log.debug("Resumen mensual actualizado para anio: {}, mes: {}", anio, mes);
    }

    /**
     * Recalcula un resumen mensual completo
     */
    private void recalcularResumen(ResumenMensual resumen, String cuentaId, Integer anio, Integer mes) {
        log.debug("Recalculando resumen para cuenta: {}, anio: {}, mes: {}", cuentaId, anio, mes);

        resumen.setTotalIngresos(BigDecimal.ZERO);
        resumen.setTotalEgresos(BigDecimal.ZERO);

        List<Transaccion> transacciones = repositorioTransaccion.obtenerTransaccionesCuenta(cuentaId);

        for (Transaccion t : transacciones) {
            LocalDateTime fecha = t.getFechaTransaccion();
            if (fecha.getYear() == anio && fecha.getMonthValue() == mes) {
                if ("INGRESO".equals(t.getTipo())) {
                    resumen.setTotalIngresos(resumen.getTotalIngresos().add(t.getMonto()));
                } else if ("GASTO".equals(t.getTipo())) {
                    resumen.setTotalEgresos(resumen.getTotalEgresos().add(t.getMonto()));
                }
            }
        }

        resumen.setSaldoFinal(resumen.getSaldoInicial().add(resumen.getTotalIngresos()).subtract(resumen.getTotalEgresos()));
    }

    /**
     * Crea un nuevo resumen mensual
     */
    private ResumenMensual crearResumenMensual(Transaccion transaccion, Integer anio, Integer mes) {
        log.debug("Creando nuevo resumen mensual para anio: {}, mes: {}", anio, mes);

        return ResumenMensual.builder()
                .usuarioId(transaccion.getUsuarioId())
                .cuentaId(transaccion.getCuentaId())
                .anio(anio)
                .mes(mes)
                .saldoInicial(BigDecimal.ZERO)
                .saldoFinal(BigDecimal.ZERO)
                .totalIngresos(BigDecimal.ZERO)
                .totalEgresos(BigDecimal.ZERO)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();
    }
}
