package model;

import java.sql.Timestamp;
import java.util.Date;

public class Admin {
	
	private final String adminname;
	private final String adminpassword;
	private final Date datetime;
	
	public Admin(String adminname, String adminpassword) {
		this.adminname = adminname;
		this.adminpassword = adminpassword;
		datetime = new java.util.Date();
	}
	
	public String toString() {
		String salida = "Admin: ("+adminname+", "+adminpassword+", "+datetime+")";
		return salida;
	}
	
	public String getAdminName() {
		return adminname;
	}	
	
	public String getAdminPassword() {
		return adminpassword;
	}

}
