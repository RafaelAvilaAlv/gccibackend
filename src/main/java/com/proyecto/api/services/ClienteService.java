package com.proyecto.api.services;

import com.proyecto.api.DTOs.ClienteRequestDto;
import com.proyecto.api.DTOs.ClienteResponseDto;

import java.util.List;

public interface ClienteService {

    List<ClienteResponseDto> listarTodos();

    List<ClienteResponseDto> listarInactivos(); // 👈 ESTE ES EL QUE TE FALTA

    ClienteResponseDto buscarPorId(Long id);

    ClienteResponseDto crear(ClienteRequestDto dto);

    ClienteResponseDto actualizar(Long id, ClienteRequestDto dto);

    void cambiarEstado(Long id, Boolean activo);
}