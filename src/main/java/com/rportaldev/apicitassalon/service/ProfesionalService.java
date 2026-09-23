package com.rportaldev.apicitassalon.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rportaldev.apicitassalon.dto.ProfesionalResponseDTO;
import com.rportaldev.apicitassalon.dto.ServicioResponseDTO;
import com.rportaldev.apicitassalon.entity.Profesional;
import com.rportaldev.apicitassalon.entity.Servicio;
import com.rportaldev.apicitassalon.repository.ProfesionalRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfesionalService {

	private final ProfesionalRepository profesionalRepository;
	
	
	public List<ProfesionalResponseDTO> buscarProfesionalesDisponibles(List<Long> servicioIds) {

	    return profesionalRepository.findAll()
	            .stream()
	            .filter(profesional -> profesionalDominaTodosLosServicios(profesional, servicioIds))
	            .map(this::convertirADTO)
	            .toList();
	}
	
	private boolean profesionalDominaTodosLosServicios(Profesional profesional, List<Long> servicioIds) {

	    List<Long> idsDelProfesional = profesional.getServicios()
	            .stream()
	            .map(Servicio::getId)
	            .toList();

	    return idsDelProfesional.containsAll(servicioIds);
	}
	
	public List<ProfesionalResponseDTO> listarProfesionales(){
		
		return profesionalRepository.findAll()
				.stream()
				.map(this::convertirADTO)
				.toList();
	}
	
	
	private ProfesionalResponseDTO convertirADTO(Profesional profesional) {
		
		List<ServicioResponseDTO> servicios = profesional.getServicios()
		        .stream()
		        .map(servicio -> new ServicioResponseDTO(
		                servicio.getId(),
		                servicio.getNombre(),
		                servicio.getDuracionMinuto(),
		                servicio.getPrecio()
		        ))
		        .toList();
		
		return new ProfesionalResponseDTO(
				profesional.getId(),
				profesional.getUsuario().getNombre(),
				profesional.getDiaInicio(),
				profesional.getDiaFin(),
				profesional.getHoraInicio(),
				profesional.getHoraFin(),
				servicios
				);
	}
}
