package com.proyecto.api.controller;

import com.proyecto.api.DTOs.ClienteRequestDto;
import com.proyecto.api.DTOs.ClienteResponseDto;
import com.proyecto.api.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping
    public List<ClienteResponseDto> listarTodos() {
        return clienteService.listarTodos();
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping("/{id}")
    public ClienteResponseDto buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @PostMapping
    public ClienteResponseDto crear(@Valid @RequestBody ClienteRequestDto dto) {
        return clienteService.crear(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @PutMapping("/{id}")
    public ClienteResponseDto actualizar(@PathVariable Long id,
                                         @Valid @RequestBody ClienteRequestDto dto) {
        return clienteService.actualizar(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/estado")
    public void cambiarEstado(@PathVariable Long id,
                              @RequestParam Boolean activo) {
        clienteService.cambiarEstado(id, activo);
    }
    
    
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping("/inactivos")
    public List<ClienteResponseDto> listarInactivos() {
        return clienteService.listarInactivos();
    }
}