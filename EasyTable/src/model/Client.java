package model;

import java.sql.Timestamp;
import java.util.Date;

public class Client {
	
	private  String username;
	private  String name;
	private  String surname1;
	private  String surname2;
	private final String mail;
	private  String password;
	private final Date datetime;
	
	public Client(String username, String name, String surname1, String surname2, String mail, String password) {
		this.username = username;
		this.name = name;
		this.surname1 = surname1;
		this.surname2 = surname2;
		this.mail = mail;
		this.password = password;
		datetime = new java.util.Date();
	}
	
	
	public String toString() {
		String salida = "Client: ("+username+", "+name+", "+surname1+", "+surname2+", "+mail+", "+password+", "+datetime+")";
		return salida;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSurname1(String surname1) {
		this.surname1 = surname1;
	}
	public void setSurname2(String surname2) {
		this.surname2 = surname2;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getMail() {
		return mail;
	}
	public String getName() {
		return name;
	}

	public String getSurname1() {
		return surname1;
	}

	public String getSurname2() {
		return surname2;
	}
	
}
