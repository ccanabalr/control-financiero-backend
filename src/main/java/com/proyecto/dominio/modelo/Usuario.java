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
@Document(collection = "usuarios")
public class Usuario {

    @Id
    private String id;

    @Field("nombre_completo")
    private String nombreCompleto;

    @Field("correo_electronico")
    private String correoElectronico;

    @Field("firebase_uid")
    private String firebaseUid;

    @Field("fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Field("fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Field("activo")
    private Boolean activo;

    @Field("moneda_preferida")
    private String monedaPreferida;
}
