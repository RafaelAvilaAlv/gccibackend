package com.proyecto.api.controller;

import com.proyecto.api.DTOs.ServicioRequestDto;
import com.proyecto.api.DTOs.ServicioResponseDto;
import com.proyecto.api.services.ServicioService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping
    public List<ServicioResponseDto> listarTodos() {
        return servicioService.listarTodos();
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping("/{id}")
    public ServicioResponseDto buscarPorId(@PathVariable Long id) {
        return servicioService.buscarPorId(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @PostMapping
    public ServicioResponseDto crear(@Valid @RequestBody ServicioRequestDto dto) {
        return servicioService.crear(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @PutMapping("/{id}")
    public ServicioResponseDto actualizar(@PathVariable Long id,
                                          @Valid @RequestBody ServicioRequestDto dto) {
        return servicioService.actualizar(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/estado")
    public void cambiarEstado(@PathVariable Long id,
                              @RequestParam Boolean activo) {
        servicioService.cambiarEstado(id, activo);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping("/inactivos")
    public List<ServicioResponseDto> listarInactivos() {
        return servicioService.listarInactivos();
    }
    
    
}