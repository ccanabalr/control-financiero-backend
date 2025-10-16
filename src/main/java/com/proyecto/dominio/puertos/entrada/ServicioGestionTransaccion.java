package com.proyecto.dominio.puertos.entrada;

import com.proyecto.dominio.modelo.Transaccion;
import java.util.List;
import java.util.Optional;

public interface ServicioGestionTransaccion {

    Transaccion crearTransaccion(Transaccion transaccion);

    Optional<Transaccion> obtenerTransaccion(String id);

    List<Transaccion> listarTransaccionesUsuario(String usuarioId);

    List<Transaccion> listarTransaccionesCuenta(String cuentaId);

    Transaccion actualizarTransaccion(String id, Transaccion transaccionActualizada);

    void eliminarTransaccion(String id);

    void recalcularResumenesMensuales(String usuarioId);
}
