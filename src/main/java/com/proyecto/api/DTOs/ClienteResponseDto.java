package com.proyecto.api.DTOs;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponseDto {

    private Long id;

    private String nombres;

    private String apellidos;

    private String telefono;

    private String email;

    private String direccion;

    private String observaciones;

    private Boolean activo;

    private LocalDateTime fechaRegistro;
}