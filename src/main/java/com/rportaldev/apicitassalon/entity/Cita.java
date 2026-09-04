package com.rportaldev.apicitassalon.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.rportaldev.apicitassalon.enums.EstadoCita;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "cita")
public class Cita {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne()
	@JoinColumn(name = "usuario_id",
				nullable = false)
	private Usuario cliente;
	
	@ManyToOne()
	@JoinColumn(name = "profesional_id",
				nullable = false)
	private Profesional profesional;
	
	@ManyToMany()
	@JoinTable(
		    name = "cita_servicio",
		    joinColumns = @JoinColumn(name = "cita_id"),
		    inverseJoinColumns = @JoinColumn(name = "servicio_id")
		)
	private List<Servicio> servicios;
	
	@Column(name = "fecha_horario_inicio",
			nullable = false)
	private LocalDateTime fechaHorarioInicio;
	
	@Column(name = "fecha_horario_fin",
			nullable = false)
	private LocalDateTime fechaHorarioFin;
	
	@Column(name = "precio_total",
			nullable = false,
			precision = 10,
			scale = 2)
	private BigDecimal precioTotal;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "estado",
			nullable = false)
	private EstadoCita estado;
}
