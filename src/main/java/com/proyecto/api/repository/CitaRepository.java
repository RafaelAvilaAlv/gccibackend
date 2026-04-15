package com.proyecto.api.repository;

import com.proyecto.api.DTOs.DashboardServicioTopDto;
import com.proyecto.api.entity.Cita;
import com.proyecto.api.enums.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByFechaOrderByHoraInicioAsc(LocalDate fecha);

    List<Cita> findByUsuarioIdAndFechaOrderByHoraInicioAsc(Long usuarioId, LocalDate fecha);

    long countByFecha(LocalDate fecha);

    List<Cita> findTop5ByFechaGreaterThanEqualOrderByFechaAscHoraInicioAsc(LocalDate fecha);

    long countByEstado(EstadoCita estado);

    long countByEstadoAndFecha(EstadoCita estado, LocalDate fecha);

    @Query("""
        SELECT c
        FROM Cita c
        WHERE c.fecha = :fecha
          AND c.usuario.id = :usuarioId
          AND c.id <> COALESCE(:citaId, -1)
          AND c.estado <> com.proyecto.api.enums.EstadoCita.CANCELADA
          AND c.estado <> com.proyecto.api.enums.EstadoCita.NO_ASISTIO
          AND c.horaInicio < :horaFin
          AND c.horaFin > :horaInicio
        """)
    List<Cita> buscarCrucesHorario(@Param("fecha") LocalDate fecha,
                                   @Param("usuarioId") Long usuarioId,
                                   @Param("horaInicio") LocalTime horaInicio,
                                   @Param("horaFin") LocalTime horaFin,
                                   @Param("citaId") Long citaId);

    @Query("""
        SELECT c
        FROM Cita c
        WHERE c.fecha >= CURRENT_DATE
        ORDER BY c.fecha ASC, c.horaInicio ASC
        """)
    List<Cita> listarProximasCitas();

    @Query("""
        SELECT c.estado, COUNT(c)
        FROM Cita c
        GROUP BY c.estado
        ORDER BY COUNT(c) DESC
        """)
    List<Object[]> obtenerCitasPorEstadoRaw();

    @Query("""
        SELECT new com.proyecto.api.DTOs.DashboardServicioTopDto(
            c.servicio.nombre,
            COUNT(c)
        )
        FROM Cita c
        GROUP BY c.servicio.nombre
        ORDER BY COUNT(c) DESC
        """)
    List<DashboardServicioTopDto> obtenerServiciosMasSolicitados();
}