package com.proyecto.dominio.modelo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "categorias")
public class Categoria {

    @Id
    private String id;

    @Field("usuario_id")
    private String usuarioId;

    @Field("nombre")
    private String nombre;

    @Field("tipo")
    private String tipo;

    @Field("color")
    private String color;

    @Field("icono")
    private String icono;

    @Field("descripcion")
    private String descripcion;

    @Field("fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Field("fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Field("activa")
    private Boolean activa;
}
