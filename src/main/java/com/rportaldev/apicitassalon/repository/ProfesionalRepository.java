package com.rportaldev.apicitassalon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rportaldev.apicitassalon.entity.Profesional;

public interface ProfesionalRepository extends JpaRepository<Profesional, Long>{

	Optional<Profesional> findByUsuarioCorreo(String correo);
}
