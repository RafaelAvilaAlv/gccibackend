package com.proyecto.api.services;

import com.proyecto.api.DTOs.PagoRequestDto;
import com.proyecto.api.DTOs.PagoResponseDto;

import java.util.List;

public interface PagoService {

    List<PagoResponseDto> listarTodos();

    PagoResponseDto registrar(PagoRequestDto dto);

    List<PagoResponseDto> listarPorCita(Long citaId);
}