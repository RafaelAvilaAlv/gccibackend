package com.proyecto.api.DTOs;



import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoRequestDto {

    @NotNull(message = "La cita es obligatoria")
    private Long citaId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;

    @Size(max = 100, message = "La referencia no debe superar los 100 caracteres")
    private String referencia;

    @Size(max = 300, message = "La observación no debe superar los 300 caracteres")
    private String observacion;
}