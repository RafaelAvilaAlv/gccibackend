package com.proyecto.api.services;

import com.proyecto.api.DTOs.CambiarEstadoCitaDto;
import com.proyecto.api.DTOs.CitaRequestDto;
import com.proyecto.api.DTOs.CitaResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface CitaService {

    List<CitaResponseDto> listarTodas();

    CitaResponseDto buscarPorId(Long id);

    CitaResponseDto crear(CitaRequestDto dto);

    CitaResponseDto actualizar(Long id, CitaRequestDto dto);

    CitaResponseDto cambiarEstado(Long id, CambiarEstadoCitaDto dto);

    List<CitaResponseDto> listarPorFecha(LocalDate fecha);

    List<CitaResponseDto> listarPorUsuarioYFecha(Long usuarioId, LocalDate fecha);

    List<CitaResponseDto> listarProximasCitas();
}