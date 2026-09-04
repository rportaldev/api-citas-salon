package com.rportaldev.apicitassalon.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ProfesionalResponseDTO {

	private Long id;
	private String nombreProfesional;
	private DayOfWeek diaInicio;
	private DayOfWeek diaFin;
	private LocalTime horaInicio;
	private LocalTime horaFin;
	private List<ServicioResponseDTO> servicios;
}
