package com.proyecto.api.DTOs;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaRequestDto {

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    @NotNull(message = "El servicio es obligatorio")
    private Long servicioId;

    @NotNull(message = "El usuario que atiende es obligatorio")
    private Long usuarioId;

    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "La fecha no puede ser pasada")
    private LocalDate fecha;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    private BigDecimal precioFinal;

    @Size(max = 300, message = "Las observaciones no deben superar los 300 caracteres")
    private String observaciones;
}