package com.rportaldev.apicitassalon.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ServicioResponseDTO {

	private Long id;
	private String nombre;
	private Integer duracionMinuto;
	private BigDecimal precio;
}
