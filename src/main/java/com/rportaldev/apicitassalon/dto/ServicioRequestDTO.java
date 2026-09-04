package com.rportaldev.apicitassalon.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ServicioRequestDTO {

	@NotBlank(message = "El nombre del servicio no puede estar vacio")
	private String nombre;
	
	@NotNull(message = "La duracion del tiempo no puede estar vacio")
	@Positive(message = "La duracion debe ser un número mayor a cero")
	private Integer duracionMinuto;
	
	@NotNull(message = "El precio no puede estar vacio")
	@DecimalMin(value = "0.01", message = "El precio debe ser un número mayor a cero")
	private BigDecimal precio;
}
