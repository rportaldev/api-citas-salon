package com.rportaldev.apicitassalon.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rportaldev.apicitassalon.dto.ServicioRequestDTO;
import com.rportaldev.apicitassalon.dto.ServicioResponseDTO;
import com.rportaldev.apicitassalon.service.ServicioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/servicios")
@RequiredArgsConstructor
public class ServicioController {


	private final ServicioService servicioService;
    
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ServicioResponseDTO> crearServicio(@RequestBody @Valid ServicioRequestDTO dto){
		
		ServicioResponseDTO response = servicioService.crearServicio(dto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ServicioResponseDTO> actualizarServicio(@PathVariable Long id, @RequestBody @Valid ServicioRequestDTO dto){
		
		ServicioResponseDTO response = servicioService.actualizarServicio(id, dto);

		return ResponseEntity.ok(response);
	}
	
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> eliminarServicio(@PathVariable Long id){
		
		servicioService.eliminarServicio(id);
		
		return ResponseEntity.noContent().build();
	}
	
	
	@GetMapping
	public ResponseEntity<List<ServicioResponseDTO>> listarServicios(){
		
		List<ServicioResponseDTO> servicios = servicioService.listarServicio();
		
		return ResponseEntity.ok(servicios);
	}
}
