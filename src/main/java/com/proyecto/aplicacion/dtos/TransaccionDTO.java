package com.proyecto.aplicacion.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransaccionDTO {

    private String id;

    private String usuarioId;

    private String cuentaId;

    private String categoriaId;

    private String tipo;

    private BigDecimal monto;

    private String descripcion;

    private LocalDateTime fechaTransaccion;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;

    private List<String> etiquetas;

    private String referenciaExterna;
}
