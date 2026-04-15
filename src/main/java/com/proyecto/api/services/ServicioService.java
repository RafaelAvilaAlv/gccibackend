package com.proyecto.api.services;

import com.proyecto.api.DTOs.ServicioRequestDto;
import com.proyecto.api.DTOs.ServicioResponseDto;

import java.util.List;

public interface ServicioService {

	List<ServicioResponseDto> listarTodos();
	
	List<ServicioResponseDto> listarInactivos();
	
	ServicioResponseDto buscarPorId(Long id);
	
	ServicioResponseDto crear(ServicioRequestDto dto);
	
	ServicioResponseDto actualizar(Long id, ServicioRequestDto dto);
	
	void cambiarEstado(Long id, Boolean activo);
}