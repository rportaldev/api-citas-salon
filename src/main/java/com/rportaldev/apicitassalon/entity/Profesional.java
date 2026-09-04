package com.rportaldev.apicitassalon.entity;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

@Entity
@Table(name = "profesional")
public class Profesional {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "usuario_id",
			nullable = false,
			unique = true)
	private Usuario usuario;
	
	@Column(name = "dia_inicio",
			nullable = false)
	private DayOfWeek diaInicio;
	
	@Column(name = "dia_fin",
			nullable = false)
	private DayOfWeek diaFin;
	
	@Column(name = "hora_inicio",
			nullable = false)
	private LocalTime horaInicio;
	
	@Column(name = "hora_fin",
			nullable = false)
	private LocalTime horaFin;
	
	@ManyToMany
	@JoinTable(
			name = "profesional_servicio",
			joinColumns = @JoinColumn(name = "profesional_id"),
			inverseJoinColumns = @JoinColumn(name = "servicio_id")
		)
	private List<Servicio> servicios;
}
