package com.proyecto.api.DTOs;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CambiarEstadoCitaDto {

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}
