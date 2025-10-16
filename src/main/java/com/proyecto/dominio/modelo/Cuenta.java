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
@Document(collection = "cuentas")
public class Cuenta {

    @Id
    private String id;

    @Field("usuario_id")
    private String usuarioId;

    @Field("nombre")
    private String nombre;

    @Field("tipo_cuenta")
    private String tipoCuenta;

    @Field("saldo_actual")
    private BigDecimal saldoActual;

    @Field("moneda")
    private String moneda;

    @Field("descripcion")
    private String descripcion;

    @Field("fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Field("fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Field("activa")
    private Boolean activa;
}
