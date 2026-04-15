package com.proyecto.api.services;

import com.proyecto.api.DTOs.DashboardCitasEstadoDto;
import com.proyecto.api.DTOs.DashboardIngresoDiarioDto;
import com.proyecto.api.DTOs.DashboardIngresosDto;
import com.proyecto.api.DTOs.DashboardProximaCitaDto;
import com.proyecto.api.DTOs.DashboardResumenDto;
import com.proyecto.api.DTOs.DashboardServicioTopDto;

import java.util.List;

public interface DashboardService {

    DashboardResumenDto obtenerResumen();

    DashboardIngresosDto obtenerIngresos();

    List<DashboardProximaCitaDto> obtenerProximasCitas();

    List<DashboardCitasEstadoDto> obtenerCitasPorEstado();

    List<DashboardIngresoDiarioDto> obtenerIngresosUltimos7Dias();

    List<DashboardServicioTopDto> obtenerServiciosMasSolicitados();
}