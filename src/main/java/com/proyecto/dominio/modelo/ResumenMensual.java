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
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "resumenes_mensuales")
public class ResumenMensual {

    @Id
    private String id;

    @Field("usuario_id")
    private String usuarioId;

    @Field("cuenta_id")
    private String cuentaId;

    @Field("anio")
    private Integer anio;

    @Field("mes")
    private Integer mes;

    @Field("saldo_inicial")
    private BigDecimal saldoInicial;

    @Field("saldo_final")
    private BigDecimal saldoFinal;

    @Field("total_ingresos")
    private BigDecimal totalIngresos;

    @Field("total_egresos")
    private BigDecimal totalEgresos;

    @Field("gasto_por_categoria")
    private Map<String, BigDecimal> gastoPorCategoria;

    @Field("ingreso_por_categoria")
    private Map<String, BigDecimal> ingresoPorCategoria;

    @Field("fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Field("fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}
