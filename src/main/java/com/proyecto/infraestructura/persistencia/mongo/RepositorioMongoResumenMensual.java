package com.proyecto.infraestructura.persistencia.mongo;

import com.proyecto.dominio.modelo.ResumenMensual;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioMongoResumenMensual extends MongoRepository<ResumenMensual, String> {

    Optional<ResumenMensual> findByCuentaIdAndAnioAndMes(String cuentaId, Integer anio, Integer mes);

    List<ResumenMensual> findByCuentaId(String cuentaId);

    List<ResumenMensual> findByUsuarioId(String usuarioId);
}
