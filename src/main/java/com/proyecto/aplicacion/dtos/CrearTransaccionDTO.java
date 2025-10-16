package com.proyecto.aplicacion.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class CrearTransaccionDTO {

    @NotBlank(message = "El ID de la cuenta es requerido")
    private String cuentaId;

    @NotBlank(message = "El tipo de transaccion es requerido (INGRESO o GASTO)")
    private String tipo;

    @NotNull(message = "El monto es requerido")
    @Positive(message = "El monto debe ser positivo")
    private BigDecimal monto;

    private String descripcion;

    private String categoriaId;

    private LocalDateTime fechaTransaccion;

    private List<String> etiquetas;
}
