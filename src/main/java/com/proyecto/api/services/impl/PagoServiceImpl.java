package com.proyecto.api.services.impl;

import com.proyecto.api.DTOs.PagoRequestDto;
import com.proyecto.api.DTOs.PagoResponseDto;
import com.proyecto.api.entity.Cita;
import com.proyecto.api.entity.Pago;
import com.proyecto.api.enums.EstadoCita;
import com.proyecto.api.enums.EstadoPago;
import com.proyecto.api.enums.MetodoPago;
import com.proyecto.api.exception.BusinessException;
import com.proyecto.api.exception.ResourceNotFoundException;
import com.proyecto.api.repository.CitaRepository;
import com.proyecto.api.repository.PagoRepository;
import com.proyecto.api.services.PagoService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final CitaRepository citaRepository;

    public PagoServiceImpl(PagoRepository pagoRepository, CitaRepository citaRepository) {
        this.pagoRepository = pagoRepository;
        this.citaRepository = citaRepository;
    }

    @Override
    public List<PagoResponseDto> listarTodos() {
        return pagoRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public PagoResponseDto registrar(PagoRequestDto dto) {
        Cita cita = obtenerCitaPorId(dto.getCitaId());

        validarEstadoCitaParaPago(cita);
        validarPagoDuplicado(cita.getId());

        MetodoPago metodoPago = convertirMetodoPago(dto.getMetodoPago());
        validarMonto(dto.getMonto(), cita);

        Pago pago = Pago.builder()
                .cita(cita)
                .monto(dto.getMonto())
                .metodoPago(metodoPago)
                .estado(EstadoPago.PAGADO) // 🔥 AQUÍ SE AGREGA
                .fechaPago(LocalDateTime.now())
                .referencia(dto.getReferencia())
                .observacion(dto.getObservacion())
                .build();

        Pago pagoGuardado = pagoRepository.save(pago);
        return mapToResponseDto(pagoGuardado);
    }

    @Override
    public List<PagoResponseDto> listarPorCita(Long citaId) {
        obtenerCitaPorId(citaId);

        return pagoRepository.findByCitaIdOrderByFechaPagoDesc(citaId)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    private Cita obtenerCitaPorId(Long citaId) {
        return citaRepository.findById(citaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cita no encontrada con id: " + citaId
                ));
    }

    private void validarEstadoCitaParaPago(Cita cita) {
        if (cita.getEstado() == EstadoCita.CANCELADA) {
            throw new BusinessException("No se puede registrar un pago para una cita cancelada");
        }

        if (cita.getEstado() != EstadoCita.COMPLETADA) {
            throw new BusinessException("Solo se puede registrar pago cuando la cita está COMPLETADA");
        }
    }

    private void validarPagoDuplicado(Long citaId) {
        if (pagoRepository.existsByCitaId(citaId)) {
            throw new BusinessException("La cita ya tiene un pago registrado");
        }
    }

    private MetodoPago convertirMetodoPago(String metodoPagoTexto) {
        if (metodoPagoTexto == null || metodoPagoTexto.trim().isEmpty()) {
            throw new BusinessException("El método de pago es obligatorio");
        }

        try {
            return MetodoPago.valueOf(metodoPagoTexto.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Método de pago inválido: " + metodoPagoTexto);
        }
    }

    private void validarMonto(BigDecimal monto, Cita cita) {
        if (monto == null) {
            throw new BusinessException("El monto del pago es obligatorio");
        }

        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El monto del pago debe ser mayor a 0");
        }

        if (cita.getPrecioFinal() != null && monto.compareTo(cita.getPrecioFinal()) > 0) {
            throw new BusinessException("El monto del pago no puede ser mayor al precio final de la cita");
        }
    }

   
    private PagoResponseDto mapToResponseDto(Pago pago) {
        return PagoResponseDto.builder()
                .id(pago.getId())
                .citaId(pago.getCita().getId())
                .monto(pago.getMonto())
                .metodoPago(pago.getMetodoPago().name())
                .estado(pago.getEstado().name())
                .fechaPago(pago.getFechaPago())
                .referencia(pago.getReferencia())
                .observacion(pago.getObservacion())

                // 🔥 NUEVOS CAMPOS
                .nombreCliente(
                    pago.getCita().getCliente().getNombres() + " " +
                    pago.getCita().getCliente().getApellidos()
                )
                .nombreServicio(
                    pago.getCita().getServicio().getNombre()
                )
                .fechaCita(
                    pago.getCita().getFecha()
                )

                .build();
    }
}