package com.proyecto.api.DTOs;



import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioRequestDto {

    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
    private String nombre;

    @Size(max = 300, message = "La descripción no debe superar los 300 caracteres")
    private String descripcion;

    @NotNull(message = "El precio base es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio base debe ser mayor a 0")
    private BigDecimal precioBase;

    @NotNull(message = "La duración es obligatoria")
    @Positive(message = "La duración debe ser mayor a 0")
    private Integer duracionMinutos;
}