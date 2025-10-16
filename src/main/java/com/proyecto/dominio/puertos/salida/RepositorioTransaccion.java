package com.proyecto.dominio.puertos.salida;

import com.proyecto.dominio.modelo.Transaccion;
import java.util.List;
import java.util.Optional;

public interface RepositorioTransaccion {

    Transaccion guardarTransaccion(Transaccion transaccion);

    Optional<Transaccion> obtenerTransaccionPorId(String id);

    List<Transaccion> obtenerTransaccionesUsuario(String usuarioId);

    List<Transaccion> obtenerTransaccionesCuenta(String cuentaId);

    List<Transaccion> obtenerTransaccionesCategoria(String categoriaId);

    void eliminarTransaccion(String id);

    void actualizar(Transaccion transaccion);
}
