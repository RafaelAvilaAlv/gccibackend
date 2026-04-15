package com.proyecto.api.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 80, message = "El nombre debe tener entre 2 y 80 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 80, message = "El apellido debe tener entre 2 y 80 caracteres")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    @Size(max = 120, message = "El email no debe superar los 120 caracteres")
    private String email;

    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    @Pattern(
            regexp = "^$|^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._#\\-])[A-Za-z\\d@$!%*?&._#\\-]+$",
            message = "La contraseña debe tener al menos una mayúscula, una minúscula, un número y un carácter especial"
    )
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "ADMIN|EMPLEADO", message = "El rol debe ser ADMIN o EMPLEADO")
    private String rol;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;
}