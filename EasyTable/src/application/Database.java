package application;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Database {
	static final String USER = "pri_easytable";
	static final String PASS = "123easytable123";
	
	public static Connection conectar_db() {
		Connection conn = null;
		try {
			
			conn = DriverManager.getConnection(
					"jbdc:mariadb://195.235.211.197/pri_easytable", USER, PASS);
			
			System.out.println("Conectando a la Base de datos...");
			return conn;
            } catch (SQLException se) {
                se.printStackTrace();
                return null;
            }//end finally try
		finally {
			System.out.println("Conexion completada");
		}
}
}