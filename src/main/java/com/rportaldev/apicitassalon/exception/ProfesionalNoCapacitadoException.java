package com.rportaldev.apicitassalon.exception;

public class ProfesionalNoCapacitadoException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ProfesionalNoCapacitadoException(String mensaje) {
		super(mensaje);
	}
}
