package com.rportaldev.apicitassalon.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CitaRequestDTO {

	@NotNull(message = "El profesional es obligatorio")
	private Long profesionalId;
	
	@NotEmpty(message = "Debe seleccionar al menos un servicio")
	private List<Long> servicioIds;
	
	@NotNull(message = "La fecha y hora de la cita es obligatoria")
	private LocalDateTime fechaHoraInicio;
	
}

