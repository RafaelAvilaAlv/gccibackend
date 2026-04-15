package com.proyecto.api.DTOs;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardProximaCitaDto {

    private Long citaId;
    private String nombreCliente;
    private String nombreServicio;
    private String nombreUsuario;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private String estado;
}