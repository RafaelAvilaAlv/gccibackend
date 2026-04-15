package com.proyecto.api.services.impl;

import com.proyecto.api.DTOs.ClienteRequestDto;
import com.proyecto.api.DTOs.ClienteResponseDto;
import com.proyecto.api.entity.Cliente;
import com.proyecto.api.repository.ClienteRepository;
import com.proyecto.api.services.ClienteService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // 🔹 LISTAR SOLO ACTIVOS
    @Override
    public List<ClienteResponseDto> listarTodos() {
        return clienteRepository.findByActivoTrue()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    // 🔹 LISTAR INACTIVOS
    @Override
    public List<ClienteResponseDto> listarInactivos() {
        return clienteRepository.findByActivoFalse()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    // 🔹 BUSCAR POR ID
    @Override
    public ClienteResponseDto buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));

        return mapToResponseDto(cliente);
    }

    // 🔹 CREAR
    @Override
    public ClienteResponseDto crear(ClienteRequestDto dto) {
        Cliente cliente = Cliente.builder()
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .direccion(dto.getDireccion())
                .observaciones(dto.getObservaciones())
                .activo(true)
                .fechaRegistro(LocalDateTime.now())
                .build();

        Cliente clienteGuardado = clienteRepository.save(cliente);

        return mapToResponseDto(clienteGuardado);
    }

    // 🔹 ACTUALIZAR
    @Override
    public ClienteResponseDto actualizar(Long id, ClienteRequestDto dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));

        cliente.setNombres(dto.getNombres());
        cliente.setApellidos(dto.getApellidos());
        cliente.setTelefono(dto.getTelefono());
        cliente.setEmail(dto.getEmail());
        cliente.setDireccion(dto.getDireccion());
        cliente.setObservaciones(dto.getObservaciones());

        Cliente clienteActualizado = clienteRepository.save(cliente);

        return mapToResponseDto(clienteActualizado);
    }

    // 🔹 ACTIVAR / DESACTIVAR (SOFT DELETE)
    @Override
    public void cambiarEstado(Long id, Boolean activo) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));

        cliente.setActivo(activo);
        clienteRepository.save(cliente);
    }

    // 🔹 MAPPER
    private ClienteResponseDto mapToResponseDto(Cliente cliente) {
        return ClienteResponseDto.builder()
                .id(cliente.getId())
                .nombres(cliente.getNombres())
                .apellidos(cliente.getApellidos())
                .telefono(cliente.getTelefono())
                .email(cliente.getEmail())
                .direccion(cliente.getDireccion())
                .observaciones(cliente.getObservaciones())
                .activo(cliente.getActivo())
                .fechaRegistro(cliente.getFechaRegistro())
                .build();
    }
}