package parquimetros;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class BDD {

	
	/*
	 * Puse todas las variables est�ticas, as� no hay que estar instanciando a �sta clase.
	 * Y adem�s aseguramos que no pueda haber m�s de 1 conexi�n en simultaneo.
	 * 
	 * No se si estar� bien hecho as�
	 */
	
	private static final String driver = "com.mysql.cj.jdbc.Driver";
	private static final String servidor = "localhost:3306";
	private static final String baseDeDatos = "parquimetros";
	private static final String url = "jdbc:mysql://"+ servidor + "/" + baseDeDatos +
							   "?serverTimezone=America/Argentina/Buenos_Aires";
	private static String usuario;
	private static String clave;
	private static Connection conexion = null;

	
	
	
	public static Connection conectar(String user, String password) throws SQLException, ClassNotFoundException {
		
		usuario = user;
		clave = password;
		Class.forName(driver);
		conexion = DriverManager.getConnection(url, usuario, clave);
		System.out.println("BDD conectada con usuario \""+usuario+"\".");			
		
		return conexion;
	}
		

	public static void desconectar() throws SQLException {		
		if (conexion != null) {
			conexion.close();
			conexion = null;
			System.out.println("BDD desconectada.");
		}
	}
	
	
	public static ResultSet ejecutarSentencia(String sql) throws SQLException {
		Statement stmt = conexion.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		return rs;
	}
	
	
	public static Connection getConexion() {		
		return conexion;		
	}
	
	public static boolean estaConectado() {
		return conexion != null;
	}
	
}