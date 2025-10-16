package com.proyecto.infraestructura.persistencia.adaptadores;

import com.proyecto.dominio.modelo.ResumenMensual;
import com.proyecto.dominio.puertos.salida.RepositorioResumenMensual;
import com.proyecto.infraestructura.persistencia.mongo.RepositorioMongoResumenMensual;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class AdaptadorPersistenciaResumenMensual implements RepositorioResumenMensual {

    private final RepositorioMongoResumenMensual repositorioMongoResumenMensual;
    private final MongoTemplate mongoTemplate;

    public AdaptadorPersistenciaResumenMensual(
            RepositorioMongoResumenMensual repositorioMongoResumenMensual,
            MongoTemplate mongoTemplate) {
        this.repositorioMongoResumenMensual = repositorioMongoResumenMensual;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ResumenMensual guardarResumen(ResumenMensual resumenMensual) {
        log.info("Guardando resumen mensual: {}", resumenMensual.getId());
        return repositorioMongoResumenMensual.save(resumenMensual);
    }

    @Override
    public Optional<ResumenMensual> obtenerResumenPorId(String id) {
        log.debug("Obteniendo resumen por ID: {}", id);
        return repositorioMongoResumenMensual.findById(id);
    }

    @Override
    public Optional<ResumenMensual> obtenerResumenPorCuentaYPeriodo(String cuentaId, Integer anio, Integer mes) {
        log.debug("Obteniendo resumen para cuenta: {}, anio: {}, mes: {}", cuentaId, anio, mes);
        return repositorioMongoResumenMensual.findByCuentaIdAndAnioAndMes(cuentaId, anio, mes);
    }

    @Override
    public List<ResumenMensual> obtenerResumenesCuenta(String cuentaId) {
        log.debug("Obteniendo resumenes de la cuenta: {}", cuentaId);
        return repositorioMongoResumenMensual.findByCuentaId(cuentaId);
    }

    @Override
    public List<ResumenMensual> obtenerResumenesUsuario(String usuarioId) {
        log.debug("Obteniendo resumenes del usuario: {}", usuarioId);
        return repositorioMongoResumenMensual.findByUsuarioId(usuarioId);
    }

    @Override
    public void actualizar(ResumenMensual resumenMensual) {
        log.info("Actualizando resumen mensual: {}", resumenMensual.getId());
        repositorioMongoResumenMensual.save(resumenMensual);
    }

    @Override
    public void eliminar(String id) {
        log.info("Eliminando resumen: {}", id);
        repositorioMongoResumenMensual.deleteById(id);
    }
}
