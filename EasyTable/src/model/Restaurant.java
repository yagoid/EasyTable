package model;

import java.sql.Timestamp;
import java.util.Date;

public class Restaurant {
	
	private final String storename;
	private final String storeaddress;
	private final String floorsuite;
	private final String namer;
	private final String surname1r;
	private final String surname2r;
	private final String mailr;
	private final String passwordr;
	private final Double valoracion;
	
	private final Double ica;
	private final Double humedad;
	private final Double temperatura;
	private final Double ruido;
	
	private final Timestamp timestamp;
	private final Date datetime;
	
	public Restaurant(String storename, String storeaddress, String floorsuite, String namer, String surname1r, String surname2r, String mailr, String passwordr, Double valoracion, 
			Double ica, Double humedad, Double temperatura, Double ruido) {
		this.storename = storename;
		this.storeaddress = storeaddress;
		this.floorsuite = floorsuite;
		this.namer = namer;
		this.surname1r = surname1r;
		this.surname2r = surname2r;
		this.mailr = mailr;
		this.passwordr = passwordr;
		this.valoracion = valoracion;
		this.ica = ica;
		this.humedad = humedad;
		this.temperatura = temperatura;
		this.ruido = ruido;
		timestamp = new Timestamp(new Date().getTime());
		datetime = new java.util.Date();
	}
	
	public String toString() {
		String salida = "Client: ("+storename+", "+storeaddress+", "+floorsuite+", "+namer+", "+surname1r+", "+surname2r+", "+mailr+", "+passwordr+", "+timestamp+", "+datetime+")";
		return salida;
	}
	
	public String getMail() {
		return mailr;
	}
	
	public String getStoreName() {
		return storename;
	}

	public String getPasswordr() {
		return passwordr;
	}
	
	public Double getValoracion() {
		return valoracion;
	}

	public Double getIca() {
		return ica;
	}

	public Double getHumedad() {
		return humedad;
	}

	public Double getTemperatura() {
		return temperatura;
	}

	public Double getRuido() {
		return ruido;
	}
	
}
	