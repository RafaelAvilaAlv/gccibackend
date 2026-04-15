package com.proyecto.api.controller;

import com.proyecto.api.DTOs.LoginRequestDto;
import com.proyecto.api.DTOs.LoginResponseDto;
import com.proyecto.api.DTOs.RegisterRequestDto;
import com.proyecto.api.entity.Rol;
import com.proyecto.api.entity.Usuario;
import com.proyecto.api.exception.BusinessException;
import com.proyecto.api.repository.RolRepository;
import com.proyecto.api.repository.UsuarioRepository;
import com.proyecto.api.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto request) {
        String emailNormalizado = request.getEmail().trim().toLowerCase();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        emailNormalizado,
                        request.getPassword()
                )
        );

        Usuario usuario = usuarioRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new BusinessException("El usuario está inactivo");
        }

        String token = jwtUtil.generateToken(
                usuario.getEmail(),
                usuario.getRol().getNombre()
        );

        return new LoginResponseDto(
                token,
                usuario.getEmail(),
                usuario.getRol().getNombre()
        );
    }

    @PostMapping("/register")
    public LoginResponseDto register(@Valid @RequestBody RegisterRequestDto request) {
        String nombre = request.getNombre().trim();
        String apellido = request.getApellido().trim();
        String email = request.getEmail().trim().toLowerCase();
        String password = request.getPassword();

        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new BusinessException("Ya existe un usuario con ese correo");
        }

        Rol rol = rolRepository.findByNombre("EMPLEADO")
                .orElseThrow(() -> new BusinessException("El rol EMPLEADO no existe"));

        Usuario usuario = Usuario.builder()
                .nombre(nombre)
                .apellido(apellido)
                .email(email)
                .password(passwordEncoder.encode(password))
                .activo(true)
                .fechaCreacion(LocalDateTime.now())
                .rol(rol)
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        String token = jwtUtil.generateToken(
                usuarioGuardado.getEmail(),
                usuarioGuardado.getRol().getNombre()
        );

        return new LoginResponseDto(
                token,
                usuarioGuardado.getEmail(),
                usuarioGuardado.getRol().getNombre()
        );
    }
}