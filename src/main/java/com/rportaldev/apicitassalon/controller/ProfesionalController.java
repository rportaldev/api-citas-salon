package com.rportaldev.apicitassalon.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rportaldev.apicitassalon.dto.ProfesionalResponseDTO;
import com.rportaldev.apicitassalon.service.ProfesionalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/profesionales")
@RequiredArgsConstructor
public class ProfesionalController {

	private final ProfesionalService profesionalService;
	
	@GetMapping
	public ResponseEntity<List<ProfesionalResponseDTO>> listarProfesionales() {

		List<ProfesionalResponseDTO> profesionales = profesionalService.listarProfesionales();
		return ResponseEntity.ok(profesionales);
	}

	@GetMapping("/disponibles")
	public ResponseEntity<List<ProfesionalResponseDTO>> buscarDisponibles(
			@RequestParam List<Long> servicioIds) {

		List<ProfesionalResponseDTO> disponibles = profesionalService.buscarProfesionalesDisponibles(servicioIds);
		return ResponseEntity.ok(disponibles);
	}
}
