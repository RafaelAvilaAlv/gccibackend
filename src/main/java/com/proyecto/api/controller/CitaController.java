package com.proyecto.api.controller;

import com.proyecto.api.DTOs.CambiarEstadoCitaDto;
import com.proyecto.api.DTOs.CitaRequestDto;
import com.proyecto.api.DTOs.CitaResponseDto;
import com.proyecto.api.services.CitaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping
    public List<CitaResponseDto> listarTodas() {
        return citaService.listarTodas();
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping("/{id}")
    public CitaResponseDto buscarPorId(@PathVariable Long id) {
        return citaService.buscarPorId(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @PostMapping
    public CitaResponseDto crear(@Valid @RequestBody CitaRequestDto dto) {
        return citaService.crear(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @PutMapping("/{id}")
    public CitaResponseDto actualizar(@PathVariable Long id,
                                      @Valid @RequestBody CitaRequestDto dto) {
        return citaService.actualizar(id, dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @PatchMapping("/{id}/estado")
    public CitaResponseDto cambiarEstado(@PathVariable Long id,
                                         @Valid @RequestBody CambiarEstadoCitaDto dto) {
        return citaService.cambiarEstado(id, dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping("/fecha/{fecha}")
    public List<CitaResponseDto> listarPorFecha(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return citaService.listarPorFecha(fecha);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping("/usuario/{usuarioId}/fecha/{fecha}")
    public List<CitaResponseDto> listarPorUsuarioYFecha(
            @PathVariable Long usuarioId,
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return citaService.listarPorUsuarioYFecha(usuarioId, fecha);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping("/proximas")
    public List<CitaResponseDto> listarProximasCitas() {
        return citaService.listarProximasCitas();
    }
}