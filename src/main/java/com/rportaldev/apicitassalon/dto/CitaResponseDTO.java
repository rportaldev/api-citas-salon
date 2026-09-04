package com.rportaldev.apicitassalon.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CitaResponseDTO {

	private Long id;
	private String correoCliente;
	private String nombreProfesional;
	private LocalDateTime fechaHorarioInicio;
	private LocalDateTime fechaHorarioFin;
	private BigDecimal precioTotal;
	private String estado;
	private List<ServicioResponseDTO> servicios;
}
