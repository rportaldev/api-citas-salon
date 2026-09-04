package com.rportaldev.apicitassalon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class AuthResponseDTO {

	private String token;
	private String tipo;
	private String correo;
	private String rol;
	
}
