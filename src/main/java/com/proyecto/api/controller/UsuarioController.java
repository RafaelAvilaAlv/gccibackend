package com.proyecto.api.controller;

import com.proyecto.api.DTOs.ChangePasswordRequestDto;
import com.proyecto.api.DTOs.UsuarioRequestDto;
import com.proyecto.api.DTOs.UsuarioResponseDto;
import com.proyecto.api.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UsuarioResponseDto> listarTodos() {
        return usuarioService.listarTodos();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public UsuarioResponseDto buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public UsuarioResponseDto crearUsuario(@Valid @RequestBody UsuarioRequestDto dto) {
        return usuarioService.crearUsuarioAdmin(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public UsuarioResponseDto actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDto dto
    ) {
        return usuarioService.actualizarUsuario(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/estado")
    public void cambiarEstado(@PathVariable Long id, @RequestParam boolean activo) {
        usuarioService.cambiarEstado(id, activo);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    @GetMapping("/me")
    public UsuarioResponseDto obtenerMiPerfil(Authentication authentication) {
        String email = authentication.getName();
        return usuarioService.obtenerUsuarioAutenticado(email);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @PutMapping("/cambiar-password")
    public void cambiarPassword(
            Authentication authentication,
            @RequestBody @Valid ChangePasswordRequestDto request
    ) {
        String email = authentication.getName();

        usuarioService.cambiarPassword(
                email,
                request.getPasswordActual(),
                request.getNuevaPassword()
        );
    }
}