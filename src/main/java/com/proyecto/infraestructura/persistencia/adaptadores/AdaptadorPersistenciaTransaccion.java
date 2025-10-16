package com.proyecto.infraestructura.persistencia.adaptadores;

import com.proyecto.dominio.modelo.Transaccion;
import com.proyecto.dominio.puertos.salida.RepositorioTransaccion;
import com.proyecto.infraestructura.persistencia.mongo.RepositorioMongoTransaccion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class AdaptadorPersistenciaTransaccion implements RepositorioTransaccion {

    private final RepositorioMongoTransaccion repositorioMongoTransaccion;
    private final MongoTemplate mongoTemplate;

    public AdaptadorPersistenciaTransaccion(
            RepositorioMongoTransaccion repositorioMongoTransaccion,
            MongoTemplate mongoTemplate) {
        this.repositorioMongoTransaccion = repositorioMongoTransaccion;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Transaccion guardarTransaccion(Transaccion transaccion) {
        log.info("Guardando transaccion: {}", transaccion.getId());
        return repositorioMongoTransaccion.save(transaccion);
    }

    @Override
    public Optional<Transaccion> obtenerTransaccionPorId(String id) {
        log.debug("Obteniendo transaccion por ID: {}", id);
        return repositorioMongoTransaccion.findById(id);
    }

    @Override
    public List<Transaccion> obtenerTransaccionesUsuario(String usuarioId) {
        log.debug("Obteniendo transacciones del usuario: {}", usuarioId);
        return repositorioMongoTransaccion.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Transaccion> obtenerTransaccionesCuenta(String cuentaId) {
        log.debug("Obteniendo transacciones de la cuenta: {}", cuentaId);
        return repositorioMongoTransaccion.findByCuentaId(cuentaId);
    }

    @Override
    public List<Transaccion> obtenerTransaccionesCategoria(String categoriaId) {
        log.debug("Obteniendo transacciones de la categoria: {}", categoriaId);
        return repositorioMongoTransaccion.findByCategoriaId(categoriaId);
    }

    @Override
    public void eliminarTransaccion(String id) {
        log.info("Eliminando transaccion: {}", id);
        repositorioMongoTransaccion.deleteById(id);
    }

    @Override
    public void actualizar(Transaccion transaccion) {
        log.info("Actualizando transaccion: {}", transaccion.getId());
        repositorioMongoTransaccion.save(transaccion);
    }
}
