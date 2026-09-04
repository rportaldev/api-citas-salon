package com.rportaldev.apicitassalon.entity;

import java.time.LocalDateTime;

import com.rportaldev.apicitassalon.enums.RolUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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
@Table(name = "usuario")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nombre",
			nullable = false)
	private String nombre;
	
	@Column(name = "correo",
			nullable = false,
			unique = true)
	private String correo;
	
	@Column(name = "password",
			nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "rol", 
			nullable = false)
	private RolUsuario rol;
	
	@Column(name = "fecha_registro",
			nullable = false)
	private LocalDateTime fechaRegistro;
	
	@PrePersist
	protected void onCreate() {
		this.fechaRegistro = LocalDateTime.now();
	}
	
}
