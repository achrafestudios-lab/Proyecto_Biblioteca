package libros;

import config.ConfigMySql;
import exception.BDException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccesoLibro {
    public static List<Libro> consultarTodosLibros() throws BDException {
        List<Libro> LibrosAux = new ArrayList<>();
        PreparedStatement ps = null;
        Connection conexion = null;

        try {
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT * FROM libro";

            ps = conexion.prepareStatement(query);

            ResultSet resultados = ps.executeQuery();

            while (resultados.next()) {
                int codigo = resultados.getInt("codigo");
                String isbn = resultados.getString("isbn");
                String titulo = resultados.getString("titulo");
                String escritor = resultados.getString("escritor");
                int anio_publicaccion = resultados.getInt("anio_publicacion");
                double puntuacion = resultados.getDouble("puntuacion");

                Libro LibroAux = new Libro(codigo, isbn, titulo, escritor, anio_publicaccion, puntuacion);

                LibrosAux.add(LibroAux);
            }
        } catch (SQLException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } catch (BDException e) {
            throw new BDException(BDException.ERROR_ABRIR_CONEXION + e.getMessage());
        } finally {
            if (conexion != null) {
                ConfigMySql.cerrarConexion(conexion);
            }
        }

        return LibrosAux;
    }

    public static void main(String[] args) {
        System.out.println(consultarTodosLibros());
    }
}
