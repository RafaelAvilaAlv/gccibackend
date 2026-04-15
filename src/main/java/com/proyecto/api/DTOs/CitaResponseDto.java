package com.proyecto.api.DTOs;



import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaResponseDto {

    private Long id;

    private Long clienteId;
    private String nombreCliente;

    private Long servicioId;
    private String nombreServicio;

    private Long usuarioId;
    private String nombreUsuario;

    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String estado;
    private BigDecimal precioFinal;
    private String observaciones;
    private LocalDateTime fechaCreacion;
}