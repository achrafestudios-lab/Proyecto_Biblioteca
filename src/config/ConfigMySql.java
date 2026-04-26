package config;

import exception.BDException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConfigMySql {

	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String URLBD = "jdbc:mysql://avnadmin:AVNS_7trtkqMqYLPQUGnVms9@biblioteca-db-proyecto-biblioteca-db.g.aivencloud.com:11135/biblioteca_db?ssl-mode=REQUIRED";

	private static final String usuario = "Ismael";
	private static final String contrasena = "AVNS_Ow1A13gr_j9m86rg3OK";

	/**
	 * Abre conexi�n con la base de datos mysql
	 * @return Connection
	 * @throws BDException BDException
	 */
	public static Connection abrirConexion() throws BDException {
		Connection conexion;
		
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
	 * @param conexion connection
	 * @throws BDException BDException
	 */
	public static void cerrarConexion(Connection conexion) throws BDException {
				
		try {
			conexion.close();
		} catch (SQLException e) {
			throw new BDException(BDException.ERROR_CERRAR_CONEXION + e.getMessage() );
		}
		
		
	}

}
