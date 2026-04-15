package com.proyecto.api.DTOs;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioResponseDto {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precioBase;
    private Integer duracionMinutos;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}