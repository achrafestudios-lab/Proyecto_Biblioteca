package config;

import exception.BDException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConfigMySql {

	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String URLBD = "jdbc:mysql://ugyb6djyxdwuyg9v:Y6VZKSljifgBGJKVtRxU@bbjn4mpo2wssuphikk8t-mysql.services.clever-cloud.com:3306/bbjn4mpo2wssuphikk8t";

	private static final String usuario = "ugyb6djyxdwuyg9v";
	private static final String contrasena = "Y6VZKSljifgBGJKVtRxU";

	/**
	 * Abre conexi�n con la base de datos mysql
	 * @return
	 * @throws BDException
	 */
	public static Connection abrirConexion() throws BDException {
		Connection conexion = null;
		
		try {
			// Carga el driver
			Class.forName(DRIVER);
			// Abre conexi�n
			conexion = DriverManager.getConnection(URLBD, usuario, contrasena);
		} 
		catch (ClassNotFoundException e) {			
			throw new BDException(BDException.ERROR_CARGAR_DRIVER + e.getMessage());
		} catch (SQLException e) {			
			throw new BDException(BDException.ERROR_ABRIR_CONEXION + e.getMessage());
		}		

		return conexion;

	}

	/**
	 * Cierra conexion con la base de datos
	 * @param conexion
	 * @throws BDException
	 */
	public static void cerrarConexion(Connection conexion) throws BDException {
				
		try {
			conexion.close();
		} catch (SQLException e) {
			throw new BDException(BDException.ERROR_CERRAR_CONEXION + e.getMessage() );
		}
		
		
	}

}
