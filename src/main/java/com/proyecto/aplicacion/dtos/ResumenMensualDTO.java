package com.proyecto.aplicacion.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumenMensualDTO {

    private String id;

    private String usuarioId;

    private String cuentaId;

    private Integer anio;

    private Integer mes;

    private BigDecimal saldoInicial;

    private BigDecimal saldoFinal;

    private BigDecimal totalIngresos;

    private BigDecimal totalEgresos;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;
}
