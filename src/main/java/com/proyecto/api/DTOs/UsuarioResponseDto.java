package com.proyecto.api.DTOs;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UsuarioResponseDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private boolean activo;
    private String rol;
    private LocalDateTime fechaCreacion;
}