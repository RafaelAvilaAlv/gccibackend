package com.proyecto.api.controller;

import com.proyecto.api.DTOs.PagoRequestDto;
import com.proyecto.api.DTOs.PagoResponseDto;
import com.proyecto.api.services.PagoService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping
    public List<PagoResponseDto> listarTodos() {
        return pagoService.listarTodos();
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @PostMapping
    public PagoResponseDto registrar(@Valid @RequestBody PagoRequestDto dto) {
        return pagoService.registrar(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping("/cita/{citaId}")
    public List<PagoResponseDto> listarPorCita(@PathVariable Long citaId) {
        return pagoService.listarPorCita(citaId);
    }
}

