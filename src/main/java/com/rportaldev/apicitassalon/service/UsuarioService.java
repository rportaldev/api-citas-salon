package com.rportaldev.apicitassalon.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rportaldev.apicitassalon.dto.UsuarioResponseDTO;
import com.rportaldev.apicitassalon.entity.Usuario;
import com.rportaldev.apicitassalon.exception.RecursoNoEncontradoException;
import com.rportaldev.apicitassalon.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;

	
	public List<UsuarioResponseDTO> listarUsuarios(){
		
		return usuarioRepository.findAll()
				.stream()
				.map(this::convertirADTO)
				.toList();
	}
	
	
	public UsuarioResponseDTO obtenerPerfil(String correo) {
		
		Usuario usuario = usuarioRepository.findByCorreo(correo)
				.orElseThrow(() -> new RecursoNoEncontradoException(
						"Usuario no encontrado con correo: " + correo));
		
		return convertirADTO(usuario);
	}
	
	
	public void eliminarUsuario(Long id) {
		
		if(!usuarioRepository.existsById(id)) {
			
			throw new RecursoNoEncontradoException(
					"Usuario no encontrado con ID: " + id);
		}
		
		usuarioRepository.deleteById(id);
	}
	
	
	private UsuarioResponseDTO convertirADTO(Usuario usuario) {

		return new UsuarioResponseDTO(
				usuario.getId(),
				usuario.getNombre(),
				usuario.getCorreo(),
				usuario.getRol().name(),
				usuario.getFechaRegistro());
	}
}
