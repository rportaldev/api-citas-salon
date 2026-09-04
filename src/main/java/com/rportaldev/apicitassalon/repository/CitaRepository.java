package com.rportaldev.apicitassalon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rportaldev.apicitassalon.entity.Cita;
import com.rportaldev.apicitassalon.enums.EstadoCita;

public interface CitaRepository extends JpaRepository<Cita, Long>{

	List<Cita> findByProfesionalIdAndEstado(Long profesionalId, EstadoCita estado);
	List<Cita> findByClienteCorreo(String correo);
	List<Cita> findByProfesionalUsuarioCorreo(String correo);
}
