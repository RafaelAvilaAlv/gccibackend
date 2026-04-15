package com.proyecto.api.services;

import com.proyecto.api.DTOs.UsuarioRequestDto;
import com.proyecto.api.DTOs.UsuarioResponseDto;

import java.util.List;

public interface UsuarioService {

    List<UsuarioResponseDto> listarTodos();

    UsuarioResponseDto buscarPorId(Long id);

    UsuarioResponseDto crearUsuarioAdmin(UsuarioRequestDto dto);

    UsuarioResponseDto actualizarUsuario(Long id, UsuarioRequestDto dto);

    void cambiarEstado(Long id, boolean activo);

    UsuarioResponseDto obtenerUsuarioAutenticado(String email);

    void cambiarPassword(String email, String passwordActual, String nuevaPassword);
}