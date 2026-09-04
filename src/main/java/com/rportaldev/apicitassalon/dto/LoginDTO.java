package com.rportaldev.apicitassalon.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class LoginDTO {

	@NotBlank(message = "El correo no puede estar vacio")
	private String correo;
	
	@NotBlank(message = "El password no puede estar vacio")
	private String password;
}
