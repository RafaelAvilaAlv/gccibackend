package com.proyecto.api.repository;

import com.proyecto.api.entity.Servicio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    List<Servicio> findByActivoTrue();
    
    List<Servicio> findByActivoFalse();
    
    long countByActivoTrue();
}