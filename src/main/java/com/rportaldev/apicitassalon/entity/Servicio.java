package com.rportaldev.apicitassalon.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "servicio")
public class Servicio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nombre",
			nullable = false)
	private String nombre;
	
	@Column(name = "duracion_minuto",
			nullable = false)
	private Integer duracionMinuto;
	
	@Column(name = "precio",
			nullable = false,
			precision = 10,
			scale = 2)
	private BigDecimal precio;
}
