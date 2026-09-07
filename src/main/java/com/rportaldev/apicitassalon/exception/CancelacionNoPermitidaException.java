package com.rportaldev.apicitassalon.exception;

public class CancelacionNoPermitidaException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public CancelacionNoPermitidaException(String mensaje) {
		super(mensaje);
	}
}
