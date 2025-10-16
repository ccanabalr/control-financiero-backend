package com.proyecto.infraestructura.persistencia.mongo;

import com.proyecto.dominio.modelo.Transaccion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioMongoTransaccion extends MongoRepository<Transaccion, String> {

    List<Transaccion> findByUsuarioId(String usuarioId);

    List<Transaccion> findByCuentaId(String cuentaId);

    List<Transaccion> findByCategoriaId(String categoriaId);
}
