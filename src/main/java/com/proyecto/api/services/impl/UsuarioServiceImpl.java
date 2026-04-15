package com.proyecto.api.services.impl;

import com.proyecto.api.DTOs.UsuarioRequestDto;
import com.proyecto.api.DTOs.UsuarioResponseDto;
import com.proyecto.api.entity.Rol;
import com.proyecto.api.entity.Usuario;
import com.proyecto.api.exception.BusinessException;
import com.proyecto.api.exception.ResourceNotFoundException;
import com.proyecto.api.repository.RolRepository;
import com.proyecto.api.repository.UsuarioRepository;
import com.proyecto.api.services.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UsuarioResponseDto> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public UsuarioResponseDto buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        return mapToResponseDto(usuario);
    }

    @Override
    public UsuarioResponseDto crearUsuarioAdmin(UsuarioRequestDto dto) {
        String nombre = normalizarTexto(dto.getNombre());
        String apellido = normalizarTexto(dto.getApellido());
        String email = normalizarEmail(dto.getEmail());
        String password = dto.getPassword() != null ? dto.getPassword().trim() : "";
        String nombreRol = dto.getRol().trim().toUpperCase();

        if (password.isBlank()) {
            throw new BusinessException("La contraseña es obligatoria para crear un usuario");
        }

        if (usuarioRepository.existsByEmail(email)) {
            throw new BusinessException("Ya existe un usuario con ese correo");
        }

        Rol rol = obtenerRolValido(nombreRol);

        Usuario usuario = Usuario.builder()
                .nombre(nombre)
                .apellido(apellido)
                .email(email)
                .password(passwordEncoder.encode(password))
                .activo(dto.getActivo())
                .fechaCreacion(LocalDateTime.now())
                .rol(rol)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        return mapToResponseDto(guardado);
    }

    @Override
    public UsuarioResponseDto actualizarUsuario(Long id, UsuarioRequestDto dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        String nombre = normalizarTexto(dto.getNombre());
        String apellido = normalizarTexto(dto.getApellido());
        String email = normalizarEmail(dto.getEmail());
        String nombreRol = dto.getRol().trim().toUpperCase();

        usuarioRepository.findByEmail(email).ifPresent(existente -> {
            if (!existente.getId().equals(id)) {
                throw new BusinessException("Ya existe otro usuario con ese correo");
            }
        });

        Rol rol = obtenerRolValido(nombreRol);

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setActivo(dto.getActivo());
        usuario.setRol(rol);

        // Si quieres permitir cambio de contraseña desde edición admin,
        // aquí podrías validar dto.getPassword() y actualizarla.
        // Por ahora no se toca password en edición administrativa.

        Usuario actualizado = usuarioRepository.save(usuario);
        return mapToResponseDto(actualizado);
    }

    @Override
    public void cambiarEstado(Long id, boolean activo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        usuario.setActivo(activo);
        usuarioRepository.save(usuario);
    }

    @Override
    public UsuarioResponseDto obtenerUsuarioAutenticado(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado con email: " + email));

        return mapToResponseDto(usuario);
    }

    @Override
    public void cambiarPassword(String email, String passwordActual, String nuevaPassword) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
            throw new BusinessException("La contraseña actual es incorrecta");
        }

        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
    }

    private Rol obtenerRolValido(String nombreRol) {
        if (!nombreRol.equals("ADMIN") && !nombreRol.equals("EMPLEADO")) {
            throw new BusinessException("El rol debe ser ADMIN o EMPLEADO");
        }

        return rolRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new BusinessException("El rol " + nombreRol + " no existe"));
    }

    private String normalizarTexto(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private String normalizarEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    private UsuarioResponseDto mapToResponseDto(Usuario usuario) {
        return UsuarioResponseDto.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .activo(Boolean.TRUE.equals(usuario.getActivo()))
                .rol(usuario.getRol().getNombre())
                .fechaCreacion(usuario.getFechaCreacion())
                .build();
    }
}