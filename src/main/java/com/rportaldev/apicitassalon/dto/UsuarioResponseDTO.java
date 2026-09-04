package com.rportaldev.apicitassalon.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class UsuarioResponseDTO {

	private Long id;
	private String nombre;
	private String correo;
	private String rol;
	private LocalDateTime fechaRegistro;

}
