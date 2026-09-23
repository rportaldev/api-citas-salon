package com.rportaldev.apicitassalon.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rportaldev.apicitassalon.dto.AuthResponseDTO;
import com.rportaldev.apicitassalon.dto.LoginDTO;
import com.rportaldev.apicitassalon.dto.RegisterDTO;
import com.rportaldev.apicitassalon.entity.Usuario;
import com.rportaldev.apicitassalon.enums.RolUsuario;
import com.rportaldev.apicitassalon.exception.CorreoYaExisteException;
import com.rportaldev.apicitassalon.exception.RecursoNoEncontradoException;
import com.rportaldev.apicitassalon.repository.UsuarioRepository;
import com.rportaldev.apicitassalon.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	
	
	public AuthResponseDTO registrar(RegisterDTO dto) {
		
		if(usuarioRepository.existsByCorreo(dto.getCorreo())) {
			
			throw new CorreoYaExisteException("El correo ya esta registrado: " + dto.getCorreo());
		}
		
		Usuario usuario = new Usuario();
		
		usuario.setNombre(dto.getNombre());
		usuario.setCorreo(dto.getCorreo());
		usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
		usuario.setRol(RolUsuario.ROLE_CLIENTE);
		
		usuarioRepository.save(usuario);
		
		String token = jwtService.generarToken(usuario);
		
		return new AuthResponseDTO(
				token,
				"Bearer",
				usuario.getCorreo(),
				usuario.getRol().name());
		
	}
	
	
	public AuthResponseDTO login(LoginDTO dto) {
		
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				dto.getCorreo(),  
				dto.getPassword()));
		
		Usuario usuario = usuarioRepository.findByCorreo(dto.getCorreo())
				.orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con correo: " + dto.getCorreo()));
		
		String token = jwtService.generarToken(usuario);
		
		return new AuthResponseDTO(
				token,
				"Bearer",
				usuario.getCorreo(),
				usuario.getRol().name());
		
	}
	
}
