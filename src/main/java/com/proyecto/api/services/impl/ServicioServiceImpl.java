package com.proyecto.api.services.impl;

import com.proyecto.api.DTOs.ServicioRequestDto;
import com.proyecto.api.DTOs.ServicioResponseDto;
import com.proyecto.api.entity.Servicio;
import com.proyecto.api.exception.BusinessException;
import com.proyecto.api.exception.ResourceNotFoundException;
import com.proyecto.api.repository.ServicioRepository;
import com.proyecto.api.services.ServicioService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository servicioRepository;

    public ServicioServiceImpl(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @Override
    public List<ServicioResponseDto> listarTodos() {
        return servicioRepository.findByActivoTrue()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public ServicioResponseDto buscarPorId(Long id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con id: " + id));

        return mapToResponseDto(servicio);
    }

    @Override
    public ServicioResponseDto crear(ServicioRequestDto dto) {
        if (servicioRepository.findAll().stream()
                .anyMatch(s -> s.getNombre().equalsIgnoreCase(dto.getNombre().trim()))) {
            throw new BusinessException("Ya existe un servicio con ese nombre");
        }

        Servicio servicio = Servicio.builder()
                .nombre(dto.getNombre().trim())
                .descripcion(dto.getDescripcion())
                .precioBase(dto.getPrecioBase())
                .duracionMinutos(dto.getDuracionMinutos())
                .activo(true)
                .fechaCreacion(LocalDateTime.now())
                .build();

        Servicio servicioGuardado = servicioRepository.save(servicio);

        return mapToResponseDto(servicioGuardado);
    }

    @Override
    public ServicioResponseDto actualizar(Long id, ServicioRequestDto dto) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con id: " + id));

        boolean existeOtroConMismoNombre = servicioRepository.findAll().stream()
                .anyMatch(s -> !s.getId().equals(id) && s.getNombre().equalsIgnoreCase(dto.getNombre().trim()));

        if (existeOtroConMismoNombre) {
            throw new BusinessException("Ya existe un servicio con ese nombre");
        }

        servicio.setNombre(dto.getNombre().trim());
        servicio.setDescripcion(dto.getDescripcion());
        servicio.setPrecioBase(dto.getPrecioBase());
        servicio.setDuracionMinutos(dto.getDuracionMinutos());

        Servicio servicioActualizado = servicioRepository.save(servicio);

        return mapToResponseDto(servicioActualizado);
    }

    @Override
    public void cambiarEstado(Long id, Boolean activo) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con id: " + id));

        servicio.setActivo(activo);
        servicioRepository.save(servicio);
    }

    private ServicioResponseDto mapToResponseDto(Servicio servicio) {
        return ServicioResponseDto.builder()
                .id(servicio.getId())
                .nombre(servicio.getNombre())
                .descripcion(servicio.getDescripcion())
                .precioBase(servicio.getPrecioBase())
                .duracionMinutos(servicio.getDuracionMinutos())
                .activo(servicio.getActivo())
                .fechaCreacion(servicio.getFechaCreacion())
                .build();
    }
    
    
    
    @Override
    public List<ServicioResponseDto> listarInactivos() {
        return servicioRepository.findByActivoFalse()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }
}