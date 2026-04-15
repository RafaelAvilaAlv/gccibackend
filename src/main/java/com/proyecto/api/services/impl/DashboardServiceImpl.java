package com.proyecto.api.services.impl;

import com.proyecto.api.DTOs.DashboardCitasEstadoDto;
import com.proyecto.api.DTOs.DashboardIngresoDiarioDto;
import com.proyecto.api.DTOs.DashboardIngresosDto;
import com.proyecto.api.DTOs.DashboardProximaCitaDto;
import com.proyecto.api.DTOs.DashboardResumenDto;
import com.proyecto.api.DTOs.DashboardServicioTopDto;
import com.proyecto.api.entity.Cita;
import com.proyecto.api.entity.Pago;
import com.proyecto.api.enums.EstadoCita;
import com.proyecto.api.repository.CitaRepository;
import com.proyecto.api.repository.ClienteRepository;
import com.proyecto.api.repository.PagoRepository;
import com.proyecto.api.repository.ServicioRepository;
import com.proyecto.api.repository.UsuarioRepository;
import com.proyecto.api.services.DashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final CitaRepository citaRepository;
    private final ClienteRepository clienteRepository;
    private final PagoRepository pagoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ServicioRepository servicioRepository;

    public DashboardServiceImpl(CitaRepository citaRepository,
                                ClienteRepository clienteRepository,
                                PagoRepository pagoRepository,
                                UsuarioRepository usuarioRepository,
                                ServicioRepository servicioRepository) {
        this.citaRepository = citaRepository;
        this.clienteRepository = clienteRepository;
        this.pagoRepository = pagoRepository;
        this.usuarioRepository = usuarioRepository;
        this.servicioRepository = servicioRepository;
    }

    @Override
    public DashboardResumenDto obtenerResumen() {
        long citasHoy = citaRepository.countByFecha(LocalDate.now());
        long clientesActivos = clienteRepository.countByActivoTrue();
        long usuariosActivos = usuarioRepository.countByActivoTrue();
        long serviciosActivos = servicioRepository.countByActivoTrue();

        long citasPendientes = citaRepository.countByEstado(EstadoCita.PENDIENTE);
        long citasConfirmadas = citaRepository.countByEstado(EstadoCita.CONFIRMADA);
        long citasCompletadas = citaRepository.countByEstado(EstadoCita.COMPLETADA);
        long citasCanceladas = citaRepository.countByEstado(EstadoCita.CANCELADA);
        long citasNoAsistio = citaRepository.countByEstado(EstadoCita.NO_ASISTIO);

        BigDecimal ingresosHoy = pagoRepository.totalIngresosHoy();
        BigDecimal ingresosMes = pagoRepository.totalIngresosMesActual();

        if (ingresosHoy == null) {
            ingresosHoy = BigDecimal.ZERO;
        }

        if (ingresosMes == null) {
            ingresosMes = BigDecimal.ZERO;
        }

        return DashboardResumenDto.builder()
                .citasHoy(citasHoy)
                .clientesActivos(clientesActivos)
                .usuariosActivos(usuariosActivos)
                .serviciosActivos(serviciosActivos)
                .citasPendientes(citasPendientes)
                .citasConfirmadas(citasConfirmadas)
                .citasCompletadas(citasCompletadas)
                .citasCanceladas(citasCanceladas)
                .citasNoAsistio(citasNoAsistio)
                .ingresosHoy(ingresosHoy)
                .ingresosMes(ingresosMes)
                .build();
    }

    @Override
    public DashboardIngresosDto obtenerIngresos() {
        BigDecimal ingresosHoy = pagoRepository.totalIngresosHoy();
        BigDecimal ingresosMes = pagoRepository.totalIngresosMesActual();

        if (ingresosHoy == null) {
            ingresosHoy = BigDecimal.ZERO;
        }

        if (ingresosMes == null) {
            ingresosMes = BigDecimal.ZERO;
        }

        return DashboardIngresosDto.builder()
                .ingresosHoy(ingresosHoy)
                .ingresosMes(ingresosMes)
                .build();
    }

    @Override
    public List<DashboardProximaCitaDto> obtenerProximasCitas() {
        List<Cita> citas = citaRepository.findTop5ByFechaGreaterThanEqualOrderByFechaAscHoraInicioAsc(LocalDate.now());

        return citas.stream()
                .map(cita -> DashboardProximaCitaDto.builder()
                        .citaId(cita.getId())
                        .nombreCliente(cita.getCliente().getNombres() + " " + cita.getCliente().getApellidos())
                        .nombreServicio(cita.getServicio().getNombre())
                        .nombreUsuario(cita.getUsuario().getNombre() + " " + cita.getUsuario().getApellido())
                        .fecha(cita.getFecha())
                        .horaInicio(cita.getHoraInicio())
                        .estado(cita.getEstado().name())
                        .build())
                .toList();
    }

    @Override
    public List<DashboardCitasEstadoDto> obtenerCitasPorEstado() {
        return citaRepository.obtenerCitasPorEstadoRaw()
                .stream()
                .map(obj -> DashboardCitasEstadoDto.builder()
                        .estado(obj[0].toString())
                        .total((Long) obj[1])
                        .build())
                .toList();
    }

    @Override
    public List<DashboardIngresoDiarioDto> obtenerIngresosUltimos7Dias() {

        LocalDate inicio = LocalDate.now().minusDays(6);
        LocalDate fin = LocalDate.now();

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime finDateTime = fin.plusDays(1).atStartOfDay();

        List<Pago> pagos = pagoRepository.obtenerPagosPorRango(inicioDateTime, finDateTime);

        // 🔥 AGRUPAR EN JAVA (SOLUCIÓN PRO)
        Map<LocalDate, BigDecimal> mapa = new HashMap<>();

        for (Pago p : pagos) {
            LocalDate fecha = p.getFechaPago().toLocalDate();

            mapa.put(
                fecha,
                mapa.getOrDefault(fecha, BigDecimal.ZERO).add(p.getMonto())
            );
        }

        // 🔥 CONVERTIR A DTO
        return mapa.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> DashboardIngresoDiarioDto.builder()
                        .fecha(e.getKey())
                        .total(e.getValue())
                        .build())
                .toList();
    }

    @Override
    public List<DashboardServicioTopDto> obtenerServiciosMasSolicitados() {
        return citaRepository.obtenerServiciosMasSolicitados();
    }
}