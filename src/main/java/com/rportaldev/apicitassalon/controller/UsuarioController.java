package com.rportaldev.apicitassalon.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rportaldev.apicitassalon.dto.UsuarioResponseDTO;
import com.rportaldev.apicitassalon.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

	private final UsuarioService usuarioService;
	
	@GetMapping("/perfil")
	public ResponseEntity<UsuarioResponseDTO> obtenerPerfil(Authentication authentication){
		
		String correo = authentication.getName();
		
		UsuarioResponseDTO perfil = usuarioService.obtenerPerfil(correo);
		
		return ResponseEntity.ok(perfil);
	}
	
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios(){
		
		List<UsuarioResponseDTO> usuarios = usuarioService.listarUsuarios();
		
		return ResponseEntity.ok(usuarios);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id){
		
		usuarioService.eliminarUsuario(id);
		return ResponseEntity.noContent().build();
	}
}
