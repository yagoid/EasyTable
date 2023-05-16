package model;

import java.util.Date;

public class Reserva {
	
	private final String localname;
	private final String clientname;
	private final String numPersonas;
	private final String fechaReserva;
	private final Date datetime;
	
	
	public Reserva(String localname, String clientname, String numPersonas, String fechaReserva) {
		super();
		this.localname = localname;
		this.clientname = clientname;
		this.numPersonas = numPersonas;
		this.fechaReserva = fechaReserva;
		datetime = new java.util.Date();
	}

	@Override
	public String toString() {
		return "Reservas [restaurante=" + localname + ", cliente=" + clientname + ", numPersonas=" + numPersonas
				+ ", fechaReserva=" + fechaReserva + ", datetime=" + datetime + "]";
	}
	

	public String getLocalname() {
		return localname;
	}


	public String getCliente() {
		return clientname;
	}


	public String getNumPersonas() {
		return numPersonas;
	}


	public String getFechaReserva() {
		return fechaReserva;
	}

}
