package com.proyecto.api.services.impl;

import com.proyecto.api.DTOs.CambiarEstadoCitaDto;
import com.proyecto.api.DTOs.CitaRequestDto;
import com.proyecto.api.DTOs.CitaResponseDto;
import com.proyecto.api.entity.Cita;
import com.proyecto.api.entity.Cliente;
import com.proyecto.api.entity.Servicio;
import com.proyecto.api.entity.Usuario;
import com.proyecto.api.enums.EstadoCita;
import com.proyecto.api.exception.BusinessException;
import com.proyecto.api.exception.ResourceNotFoundException;
import com.proyecto.api.repository.CitaRepository;
import com.proyecto.api.repository.ClienteRepository;
import com.proyecto.api.repository.ServicioRepository;
import com.proyecto.api.repository.UsuarioRepository;
import com.proyecto.api.services.CitaService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final ClienteRepository clienteRepository;
    private final ServicioRepository servicioRepository;
    private final UsuarioRepository usuarioRepository;

    public CitaServiceImpl(CitaRepository citaRepository,
                           ClienteRepository clienteRepository,
                           ServicioRepository servicioRepository,
                           UsuarioRepository usuarioRepository) {
        this.citaRepository = citaRepository;
        this.clienteRepository = clienteRepository;
        this.servicioRepository = servicioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<CitaResponseDto> listarTodas() {
        return citaRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public CitaResponseDto buscarPorId(Long id) {
        Cita cita = obtenerCitaPorId(id);
        return mapToResponseDto(cita);
    }

    @Override
    public CitaResponseDto crear(CitaRequestDto dto) {
        Cliente cliente = obtenerClienteActivo(dto.getClienteId());
        Servicio servicio = obtenerServicioActivo(dto.getServicioId());
        Usuario usuario = obtenerUsuarioActivo(dto.getUsuarioId());

        validarHorario(dto);
        validarCruceHorario(
                dto.getFecha(),
                usuario.getId(),
                dto.getHoraInicio(),
                dto.getHoraFin(),
                null
        );
        validarPrecioFinal(dto.getPrecioFinal(), servicio);

        Cita cita = Cita.builder()
                .cliente(cliente)
                .servicio(servicio)
                .usuario(usuario)
                .fecha(dto.getFecha())
                .horaInicio(dto.getHoraInicio())
                .horaFin(dto.getHoraFin())
                .estado(EstadoCita.PENDIENTE)
                .precioFinal(obtenerPrecioFinal(dto.getPrecioFinal(), servicio))
                .observaciones(dto.getObservaciones())
                .fechaCreacion(LocalDateTime.now())
                .build();

        Cita citaGuardada = citaRepository.save(cita);
        return mapToResponseDto(citaGuardada);
    }
    
    
    private void validarEdicionSegunEstado(Cita cita) {
        if (cita.getEstado() == EstadoCita.COMPLETADA
                || cita.getEstado() == EstadoCita.CANCELADA
                || cita.getEstado() == EstadoCita.NO_ASISTIO) {
            throw new BusinessException("No se puede editar una cita con estado " + cita.getEstado());
        }
    }

    
    @Override
    public CitaResponseDto actualizar(Long id, CitaRequestDto dto) {
        Cita cita = obtenerCitaPorId(id);

        validarEdicionSegunEstado(cita);

        Cliente cliente = obtenerClienteActivo(dto.getClienteId());
        Servicio servicio = obtenerServicioActivo(dto.getServicioId());
        Usuario usuario = obtenerUsuarioActivo(dto.getUsuarioId());

        validarHorario(dto);
        validarCruceHorario(
                dto.getFecha(),
                usuario.getId(),
                dto.getHoraInicio(),
                dto.getHoraFin(),
                id
        );
        validarPrecioFinal(dto.getPrecioFinal(), servicio);

        cita.setCliente(cliente);
        cita.setServicio(servicio);
        cita.setUsuario(usuario);
        cita.setFecha(dto.getFecha());
        cita.setHoraInicio(dto.getHoraInicio());
        cita.setHoraFin(dto.getHoraFin());
        cita.setPrecioFinal(obtenerPrecioFinal(dto.getPrecioFinal(), servicio));
        cita.setObservaciones(dto.getObservaciones());

        Cita citaActualizada = citaRepository.save(cita);
        return mapToResponseDto(citaActualizada);
    }

    @Override
    public CitaResponseDto cambiarEstado(Long id, CambiarEstadoCitaDto dto) {
        Cita cita = obtenerCitaPorId(id);

        EstadoCita nuevoEstado;
        try {
            nuevoEstado = EstadoCita.valueOf(dto.getEstado().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Estado de cita inválido: " + dto.getEstado());
        }

        validarCambioEstado(cita.getEstado(), nuevoEstado);

        cita.setEstado(nuevoEstado);

        Cita citaActualizada = citaRepository.save(cita);
        return mapToResponseDto(citaActualizada);
    }
    
    

    @Override
    public List<CitaResponseDto> listarPorFecha(LocalDate fecha) {
        return citaRepository.findByFechaOrderByHoraInicioAsc(fecha)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public List<CitaResponseDto> listarPorUsuarioYFecha(Long usuarioId, LocalDate fecha) {
        return citaRepository.findByUsuarioIdAndFechaOrderByHoraInicioAsc(usuarioId, fecha)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public List<CitaResponseDto> listarProximasCitas() {
        return citaRepository.listarProximasCitas()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    private Cita obtenerCitaPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con id: " + id));
    }

    private Cliente obtenerClienteActivo(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + clienteId));

        if (!Boolean.TRUE.equals(cliente.getActivo())) {
            throw new BusinessException("No se puede agendar una cita con un cliente inactivo");
        }

        return cliente;
    }

    private Servicio obtenerServicioActivo(Long servicioId) {
        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con id: " + servicioId));

        if (!Boolean.TRUE.equals(servicio.getActivo())) {
            throw new BusinessException("No se puede agendar una cita con un servicio inactivo");
        }

        return servicio;
    }

    private Usuario obtenerUsuarioActivo(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new BusinessException("No se puede agendar una cita con un usuario inactivo");
        }

        return usuario;
    }

    private void validarHorario(CitaRequestDto dto) {
        if (dto.getFecha() == null) {
            throw new BusinessException("La fecha de la cita es obligatoria");
        }

        if (dto.getHoraInicio() == null || dto.getHoraFin() == null) {
            throw new BusinessException("La hora de inicio y la hora de fin son obligatorias");
        }

        if (!dto.getHoraInicio().isBefore(dto.getHoraFin())) {
            throw new BusinessException("La hora de inicio debe ser menor que la hora de fin");
        }

        LocalDateTime fechaHoraInicio = LocalDateTime.of(dto.getFecha(), dto.getHoraInicio());
        if (fechaHoraInicio.isBefore(LocalDateTime.now())) {
            throw new BusinessException("No se puede registrar una cita en fecha u hora pasada");
        }
    }

    private void validarCruceHorario(LocalDate fecha,
                                     Long usuarioId,
                                     LocalTime horaInicio,
                                     LocalTime horaFin,
                                     Long citaId) {
        List<Cita> cruces = citaRepository.buscarCrucesHorario(
                fecha,
                usuarioId,
                horaInicio,
                horaFin,
                citaId
        );
        
        if (!cruces.isEmpty()) {
            throw new BusinessException("Ya existe una cita que se cruza en ese horario para el empleado seleccionado");
        }
    }

    private void validarPrecioFinal(BigDecimal precioFinal, Servicio servicio) {
        BigDecimal precio = obtenerPrecioFinal(precioFinal, servicio);

        if (precio.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("El precio final no puede ser negativo");
        }
    }

    private BigDecimal obtenerPrecioFinal(BigDecimal precioFinal, Servicio servicio) {
        return precioFinal != null ? precioFinal : servicio.getPrecioBase();
    }

    private void validarCambioEstado(EstadoCita estadoActual, EstadoCita nuevoEstado) {

        // No permitir cambiar si ya está completada
        if (estadoActual == EstadoCita.COMPLETADA) {
            throw new BusinessException("No se puede cambiar el estado de una cita completada");
        }

        // Reglas desde PENDIENTE
        if (estadoActual == EstadoCita.PENDIENTE) {
            if (!(nuevoEstado == EstadoCita.CONFIRMADA ||
                  nuevoEstado == EstadoCita.CANCELADA ||
                  nuevoEstado == EstadoCita.NO_ASISTIO)) {

                throw new BusinessException(
                    "Una cita PENDIENTE solo puede cambiar a CONFIRMADA, CANCELADA o NO_ASISTIO"
                );
            }
        }

        // Reglas desde CONFIRMADA
        if (estadoActual == EstadoCita.CONFIRMADA) {
            if (!(nuevoEstado == EstadoCita.COMPLETADA ||
                  nuevoEstado == EstadoCita.CANCELADA ||
                  nuevoEstado == EstadoCita.NO_ASISTIO)) {

                throw new BusinessException(
                    "Una cita CONFIRMADA solo puede cambiar a COMPLETADA, CANCELADA o NO_ASISTIO"
                );
            }
        }
    }

    private CitaResponseDto mapToResponseDto(Cita cita) {
        return CitaResponseDto.builder()
                .id(cita.getId())
                .clienteId(cita.getCliente().getId())
                .nombreCliente(cita.getCliente().getNombres() + " " + cita.getCliente().getApellidos())
                .servicioId(cita.getServicio().getId())
                .nombreServicio(cita.getServicio().getNombre())
                .usuarioId(cita.getUsuario().getId())
                .nombreUsuario(cita.getUsuario().getNombre() + " " + cita.getUsuario().getApellido())
                .fecha(cita.getFecha())
                .horaInicio(cita.getHoraInicio())
                .horaFin(cita.getHoraFin())
                .estado(cita.getEstado().name())
                .precioFinal(cita.getPrecioFinal())
                .observaciones(cita.getObservaciones())
                .fechaCreacion(cita.getFechaCreacion())
                .build();
    }
}