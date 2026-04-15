package com.proyecto.api.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResumenDto {

    private Long citasHoy;
    private Long clientesActivos;
    private Long usuariosActivos;
    private Long serviciosActivos;

    private Long citasPendientes;
    private Long citasConfirmadas;
    private Long citasCompletadas;
    private Long citasCanceladas;
    private Long citasNoAsistio;

    private BigDecimal ingresosHoy;
    private BigDecimal ingresosMes;
}