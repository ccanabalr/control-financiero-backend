package com.proyecto.dominio.modelo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "transacciones")
public class Transaccion {

    @Id
    private String id;

    @Field("usuario_id")
    private String usuarioId;

    @Field("cuenta_id")
    private String cuentaId;

    @Field("categoria_id")
    private String categoriaId;

    @Field("tipo")
    private String tipo;

    @Field("monto")
    private BigDecimal monto;

    @Field("descripcion")
    private String descripcion;

    @Field("fecha_transaccion")
    private LocalDateTime fechaTransaccion;

    @Field("fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Field("fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Field("etiquetas")
    private java.util.List<String> etiquetas;

    @Field("referencia_externa")
    private String referenciaExterna;
}
