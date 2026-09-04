package com.rportaldev.apicitassalon.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class RegisterDTO {

	@NotBlank(message = "El nombre es obligatorio")
	private String nombre;
	
	@NotBlank(message = "El correo es obligatorio")
	@Email(message = "El correo no es valido")
	private String correo;
	
	@NotBlank(message = "El password es obligatorio")
	@Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
	private String password;
}
