package com.proyecto.api.DTOs;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoResponseDto {

    private Long id;
    private Long citaId;
    private BigDecimal monto;
    private String metodoPago;
    private String estado; // 🔥 NUEVO CAMPO
    private LocalDateTime fechaPago;
    private String referencia;
    private String observacion;
    
    
    private String nombreCliente;
    private String nombreServicio;
    private LocalDate fechaCita;
}