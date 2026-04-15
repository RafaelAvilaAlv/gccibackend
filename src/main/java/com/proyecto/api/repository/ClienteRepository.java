package com.proyecto.api.repository;

import com.proyecto.api.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    long countByActivoTrue();

    List<Cliente> findByActivoTrue();

    List<Cliente> findByActivoFalse();
}