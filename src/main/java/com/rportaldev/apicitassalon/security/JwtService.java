package com.rportaldev.apicitassalon.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.rportaldev.apicitassalon.entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	
	private SecretKey obtenerClaveFirma() {
		
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}
	

	private Claims extraerClaims(String token) {
		
		return Jwts.parser()
				.verifyWith(obtenerClaveFirma())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	
	private boolean tokenExpirado(String token) {
		
		Date fechaExpiracion = extraerClaims(token).getExpiration();
		
		return fechaExpiracion.before(new Date());
	}
	
	
	public String generarToken(Usuario usuario) {
		
		return Jwts.builder()
				.subject(usuario.getCorreo())
				.issuedAt(new Date())
				.expiration(
						new Date(
								System.currentTimeMillis() + expiration))
				.signWith(obtenerClaveFirma())
				.compact();
				
	}
	
	
	public String extraerCorreo(String token) {
		
		return extraerClaims(token)
				.getSubject();
	}
	
	
	public boolean validarToken(String token, UserDetails userDetails) {
		
		String correo = extraerCorreo(token);
		
		return correo.equals(userDetails.getUsername()) && !tokenExpirado(token);
	}
	
}
