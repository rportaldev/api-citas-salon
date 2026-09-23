package com.rportaldev.apicitassalon.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rportaldev.apicitassalon.dto.CitaRequestDTO;
import com.rportaldev.apicitassalon.dto.CitaResponseDTO;
import com.rportaldev.apicitassalon.service.CitaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/citas")
@RequiredArgsConstructor
public class CitaController {
	
	private final CitaService citaService;

	@PostMapping
	public ResponseEntity<CitaResponseDTO> crearCita(@RequestBody @Valid CitaRequestDTO dto,
			Authentication authentication) {

		String correo = authentication.getName();
		CitaResponseDTO response = citaService.crearCita(correo, dto);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> cancelarCita(@PathVariable Long id,
			Authentication authentication) {

		String correo = authentication.getName();
		citaService.cancelarCita(correo, id);

		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/mias")
	public ResponseEntity<List<CitaResponseDTO>> misCitas(Authentication authentication) {

		String correo = authentication.getName();
		List<CitaResponseDTO> citas = citaService.listarMisCitas(correo);

		return ResponseEntity.ok(citas);
	}
	
	@GetMapping("/profesional")
	@PreAuthorize("hasRole('PROFESIONAL')")
	public ResponseEntity<List<CitaResponseDTO>> citasDeProfesional(Authentication authentication) {

		String correo = authentication.getName();
		List<CitaResponseDTO> citas = citaService.listarCitasDeProfesional(correo);

		return ResponseEntity.ok(citas);
	}
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<CitaResponseDTO>> listarTodas() {

		List<CitaResponseDTO> citas = citaService.listarTodasLasCitas();
		return ResponseEntity.ok(citas);
	}
}
