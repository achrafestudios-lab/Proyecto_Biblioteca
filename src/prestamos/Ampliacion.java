package prestamos;

import config.ConfigMySql;
import exception.AmpliacionException;
import exception.BDException;
import exception.PrestamosException;
import exception.SociosException;
import libros.Libro;
import socios.Socio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static prestamos.AccesoPrestamo.prestamos;

public class Ampliacion {

    public static void ConsultarLibrosMenosPrestados (){

    }

    public static void consultarSociosConMasPrestamos (){

    }

    public static void consultarLibrosBajoLaMediaDePrestamos (){

    }

    public static List<String> consultarSociosSobreLaMediaDePrestamos (){
        List<String> socios = new LinkedList<>();
        Connection conexion = null;
        PreparedStatement ps;

        try {
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT s.dni AS dni_socio, s.nombre AS nombre_socio, COUNT(p.codigo_libro) AS total " +
                    "FROM socio s " +
                    "JOIN prestamo p ON s.codigo = p.codigo_socio " +
                    "GROUP BY s.dni, s.nombre " +
                    "HAVING total > (SELECT AVG(conteo) " +
                    "FROM (SELECT COUNT(*) AS conteo FROM prestamo GROUP BY codigo_socio) AS sub) " +
                    "ORDER BY total DESC";

            ps = conexion.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            extraerRankingSocios(socios, rs);

            if (socios.isEmpty()) {
                throw new AmpliacionException(AmpliacionException.LISTA_SOCIOS_VACIA_EXCEPTION);
            }

        } catch (SQLException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } catch (AmpliacionException e) {
            throw new RuntimeException(e);
        } finally {
            if (conexion != null) {
                ConfigMySql.cerrarConexion(conexion);
            }
        }

        return socios;
    }

    public static List <String> consultarRankingLibrosPrestados (){
        List<String> libros = new LinkedList<>();
        Connection conexion = null;
        PreparedStatement ps;

        try {
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT l.isbn AS isbn_libro, l.titulo AS titulo_libro, COUNT(p.codigo_libro) AS total " +
                    "FROM libro l " +
                    "JOIN prestamo p ON l.codigo = p.codigo_libro " +
                    "GROUP BY l.isbn, l.titulo " +
                    "ORDER BY total DESC";

            ps = conexion.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            crearLibro(libros, rs);

            if (libros.isEmpty()) {
                throw new AmpliacionException(AmpliacionException.LISTA_LIBROS_VACIA_EXCEPTION);
            }
        } catch (SQLException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } catch (AmpliacionException e) {
            throw new RuntimeException(e);
        } finally {
            if (conexion != null) {
                ConfigMySql.cerrarConexion(conexion);
            }
        }

        return libros;
    }

    public static List <String> consultarRankingSociosPorPrestamos(){
        List<String> socios = new LinkedList<>();
        Connection conexion = null;
        PreparedStatement ps;

        try {
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT s.dni AS dni_socio, s.nombre AS nombre_socio, COUNT(p.codigo_libro) AS total " +
                    "FROM socio s " +
                    "JOIN prestamo p ON s.codigo = p.codigo_socio " +
                    "GROUP BY s.dni, s.nombre " +
                    "ORDER BY total DESC";

            ps = conexion.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            crearSocio(socios, rs);

            if (socios.isEmpty()) {
                throw new AmpliacionException(AmpliacionException.LISTA_SOCIOS_VACIA_EXCEPTION);
            }
        } catch (SQLException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } catch (BDException e) {
            throw new BDException(BDException.ERROR_ABRIR_CONEXION + e.getMessage());
        } catch (AmpliacionException e) {
            throw new RuntimeException(e);
        } finally {
            if (conexion != null) {
                ConfigMySql.cerrarConexion(conexion);
            }
        }

        return socios;
    }

    private static void crearSocio(List<String> socios, ResultSet rs) throws SQLException {
        while (rs.next()) {
            String dni = rs.getString("dni_socio");
            String nombre = rs.getString("nombre_socio");
            int num_prestamos = rs.getInt("total");

            String cadena = "Socio [DNI: " + dni + ", Nombre: " + nombre + ", Prestamos: " + num_prestamos + "]";
            socios.add(cadena);
        }
    }

    private static void crearLibro(List<String> libros, ResultSet rs) throws SQLException {
        while (rs.next()) {
            // Usamos los alias definidos en la consulta para evitar errores
            String isbn = rs.getString("isbn_libro");
            String titulo = rs.getString("titulo_libro");
            int total = rs.getInt("total");

            String cadena = "Libro [ISBN: " + isbn + ", Título: " + titulo + ", Veces prestado: " + total + "]";
            libros.add(cadena);
        }
    }

    private static void extraerRankingSocios(List<String> socios, ResultSet rs) throws SQLException {
        while (rs.next()) {
            String dni = rs.getString("dni_socio");
            String nombre = rs.getString("nombre_socio");
            int total = rs.getInt("total");

            String cadena = "Socio [DNI: " + dni + ", Nombre: " + nombre + ", Préstamos: " + total + "]";
            socios.add(cadena);
        }
    }
}
