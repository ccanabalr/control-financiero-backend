package com.proyecto.dominio.puertos.salida;

import com.proyecto.dominio.modelo.ResumenMensual;
import java.util.List;
import java.util.Optional;

public interface RepositorioResumenMensual {

    ResumenMensual guardarResumen(ResumenMensual resumenMensual);

    Optional<ResumenMensual> obtenerResumenPorId(String id);

    Optional<ResumenMensual> obtenerResumenPorCuentaYPeriodo(String cuentaId, Integer anio, Integer mes);

    List<ResumenMensual> obtenerResumenesCuenta(String cuentaId);

    List<ResumenMensual> obtenerResumenesUsuario(String usuarioId);

    void actualizar(ResumenMensual resumenMensual);

    void eliminar(String id);
}
