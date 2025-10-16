package com.proyecto.infraestructura.persistencia.adaptadores;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UtilUpsertAtomica {

    private final MongoTemplate mongoTemplate;

    public UtilUpsertAtomica(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Realiza una operacion de upsert atomica en MongoDB.
     * Incrementa campos numericos y establece valores en caso de insercion.
     *
     * @param query Query para identificar el documento
     * @param update Update con operaciones $inc y $setOnInsert
     * @param entityClass Clase del documento
     * @return El resultado del upsert
     */
    public void ejecutarUpsert(Query query, Update update, Class<?> entityClass) {
        log.debug("Ejecutando upsert atomico para clase: {}", entityClass.getSimpleName());
        mongoTemplate.upsert(query, update, entityClass);
    }
}
