package com.proyecto.api.controller;

import com.proyecto.api.DTOs.DashboardIngresosDto;
import com.proyecto.api.DTOs.DashboardProximaCitaDto;
import com.proyecto.api.DTOs.DashboardResumenDto;

// 🔥 NUEVOS IMPORTS
import com.proyecto.api.DTOs.DashboardCitasEstadoDto;
import com.proyecto.api.DTOs.DashboardIngresoDiarioDto;
import com.proyecto.api.DTOs.DashboardServicioTopDto;

import com.proyecto.api.services.DashboardService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping("/resumen")
    public DashboardResumenDto obtenerResumen() {
        return dashboardService.obtenerResumen();
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping("/ingresos")
    public DashboardIngresosDto obtenerIngresos() {
        return dashboardService.obtenerIngresos();
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping("/proximas-citas")
    public List<DashboardProximaCitaDto> obtenerProximasCitas() {
        return dashboardService.obtenerProximasCitas();
    }

    // ==============================
    // 🔥 NUEVOS ENDPOINTS
    // ==============================

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping("/citas-estado")
    public List<DashboardCitasEstadoDto> obtenerCitasPorEstado() {
        return dashboardService.obtenerCitasPorEstado();
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping("/ingresos-ultimos-7-dias")
    public List<DashboardIngresoDiarioDto> obtenerIngresosUltimos7Dias() {
        return dashboardService.obtenerIngresosUltimos7Dias();
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping("/servicios-top")
    public List<DashboardServicioTopDto> obtenerServiciosMasSolicitados() {
        return dashboardService.obtenerServiciosMasSolicitados();
    }
}