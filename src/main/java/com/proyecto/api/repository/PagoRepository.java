package com.proyecto.api.repository;

import com.proyecto.api.DTOs.DashboardIngresoDiarioDto;
import com.proyecto.api.entity.Pago;
import com.proyecto.api.enums.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByCitaIdOrderByFechaPagoDesc(Long citaId);

    boolean existsByCitaId(Long citaId);

    List<Pago> findByMetodoPago(MetodoPago metodoPago);

    @Query("""
           SELECT COALESCE(SUM(p.monto), 0)
           FROM Pago p
           WHERE p.cita.id = :citaId
           """)
    BigDecimal totalPagadoPorCita(@Param("citaId") Long citaId);

    @Query("""
           SELECT COALESCE(SUM(p.monto), 0)
           FROM Pago p
           WHERE DATE(p.fechaPago) = CURRENT_DATE
           """)
    BigDecimal totalIngresosHoy();

    @Query("""
           SELECT COALESCE(SUM(p.monto), 0)
           FROM Pago p
           WHERE YEAR(p.fechaPago) = YEAR(CURRENT_DATE)
             AND MONTH(p.fechaPago) = MONTH(CURRENT_DATE)
           """)
    BigDecimal totalIngresosMesActual();

    @Query("""
           SELECT COALESCE(SUM(p.monto), 0)
           FROM Pago p
           WHERE p.fechaPago BETWEEN :fechaInicio AND :fechaFin
           """)
    BigDecimal totalIngresosPorRango(@Param("fechaInicio") LocalDateTime fechaInicio,
                                     @Param("fechaFin") LocalDateTime fechaFin);

    List<Pago> findByFechaPagoBetweenOrderByFechaPagoDesc(LocalDateTime fechaInicio,
                                                          LocalDateTime fechaFin);

    
    @Query("""
    	    SELECT p
    	    FROM Pago p
    	    WHERE p.fechaPago BETWEEN :inicio AND :fin
    	""")
    	List<Pago> obtenerPagosPorRango(
    	        @Param("inicio") LocalDateTime inicio,
    	        @Param("fin") LocalDateTime fin
    	);
}