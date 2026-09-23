package com.rportaldev.apicitassalon.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rportaldev.apicitassalon.dto.ServicioRequestDTO;
import com.rportaldev.apicitassalon.dto.ServicioResponseDTO;
import com.rportaldev.apicitassalon.entity.Servicio;
import com.rportaldev.apicitassalon.exception.RecursoNoEncontradoException;
import com.rportaldev.apicitassalon.repository.ServicioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServicioService {

	private final ServicioRepository servicioRepository;
	
	
	public ServicioResponseDTO crearServicio(ServicioRequestDTO dto) {
		
		Servicio servicio = new Servicio();
		
		servicio.setNombre(dto.getNombre());
		servicio.setDuracionMinuto(dto.getDuracionMinuto());
		servicio.setPrecio(dto.getPrecio());
		
		servicioRepository.save(servicio);
		
		return convertirADTO(servicio);
	}
	
	
	public List<ServicioResponseDTO> listarServicio(){
		
		return servicioRepository.findAll()
				.stream()
				.map(this::convertirADTO)
				.toList();
	}
	
	
	public ServicioResponseDTO actualizarServicio(Long id, ServicioRequestDTO dto) {
		
		Servicio servicio = servicioRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException(
						"Servicio no encontrado con ID: " + id));
		
		servicio.setNombre(dto.getNombre());
		servicio.setDuracionMinuto(dto.getDuracionMinuto());
		servicio.setPrecio(dto.getPrecio());
		
		servicioRepository.save(servicio);
		
		return convertirADTO(servicio);
		
	}
	
	
	public void eliminarServicio(Long id) {
		
		if(!servicioRepository.existsById(id)) {
			throw new RecursoNoEncontradoException(
					"Servicio no encontrado con ID: " + id);
		}
		
		servicioRepository.deleteById(id);
	}
	
	
	private ServicioResponseDTO convertirADTO(Servicio servicio) {
		
		return new ServicioResponseDTO(
				servicio.getId(),
				servicio.getNombre(),
				servicio.getDuracionMinuto(),
				servicio.getPrecio()
				);
	}
}
