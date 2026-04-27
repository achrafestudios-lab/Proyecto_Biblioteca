package prestamos;

import config.ConfigMySql;
import exception.BDException;
import libros.Libro;
import socios.Socio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    public static List<Libro> consultarLibrosMenosPrestados (){
        Connection conexion = null;
        PreparedStatement ps = null;
        List<Libro> libros = new LinkedList<>();

        try{
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT l.* " +
                    " FROM prestamo p " +
                    " JOIN libro l ON p.codigo_libro = l.codigo " +
                    " GROUP BY l.codigo, l.isbn, l.titulo, l.escritor, l.anio_publicacion, l.puntuacion " +
                    " HAVING COUNT(*) = (SELECT MIN(COUNT(*)) FROM prestamo GROUP BY codigo_libro)";
            ps = conexion.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            crearLibro(libros, rs);

            if(libros.isEmpty()){

            }
        }catch (SQLException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } catch (BDException e) {
            throw new BDException(BDException.ERROR_ABRIR_CONEXION + e.getMessage());
        } finally {
            if (conexion != null) {
                ConfigMySql.cerrarConexion(conexion);
            }
        }

        return libros;
    }

    public static List<Socio> consultarSociosConMasPrestamos (){
        Connection conexion = null;
        PreparedStatement ps = null;
        List<Socio> socios = new LinkedList<>();

        try {
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT s.* FROM prestamo" +
                    " JOIN socio s ON (p.codigo_socio = s.codigo)" +
                    " WHERE p.codigo_socio IN (SELECT * FROM prestamo GROUP BY codigo_socio HAVING MAX(COUNT(*)))";
            ps = conexion.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            crearSocio(socios, rs);

            if(socios.isEmpty()){

            }
        }catch (SQLException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } catch (BDException e) {
            throw new BDException(BDException.ERROR_ABRIR_CONEXION + e.getMessage());
        } finally {
            if (conexion != null) {
                ConfigMySql.cerrarConexion(conexion);
            }
        }

        return socios;
    }

    public static List<Libro> consultarLibrosBajoLaMediaDePrestamos (){
        Connection conexion = null;
        PreparedStatement ps = null;
        List<Libro> libros = new LinkedList<>();
        try {
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT l.* FROM prestamo p" +
                    " JOIN libro l ON (p.codigo_libro = l.codigo)" +
                    " GROUP BY p.codigo_libro" +
                    " HAVING COUNT(*) < (SELECT AVG(C))";
            ps=conexion.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            crearLibro(libros, rs);

            if(libros.isEmpty()){

            }
        }catch (SQLException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } catch (BDException e) {
            throw new BDException(BDException.ERROR_ABRIR_CONEXION + e.getMessage());
        } finally {
            if (conexion != null) {
                ConfigMySql.cerrarConexion(conexion);
            }
        }

        return libros;
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

    }

    /**
     * Este metodo crea objetos Libro a partir de un ResultSet y los agrega a una lista
     *
     * @param librosAux  Lista donde se almacenan los libros creados
     * @param resultados ResultSet con los datos de la consulta
     * @throws SQLException Gestion de excepciones
     */
    private static void crearLibro(List<Libro> librosAux, ResultSet resultados) throws SQLException {
        while (resultados.next()) {
            int codigo = resultados.getInt("codigo");
            String isbn = resultados.getString("isbn");
            String titulo = resultados.getString("titulo");
            String escritor = resultados.getString("escritor");
            int anio_publicaccion = resultados.getInt("anio_publicacion");
            double puntuacion = resultados.getDouble("puntuacion");

            Libro LibroAux = new Libro(codigo, isbn, titulo, escritor, anio_publicaccion, puntuacion);

            librosAux.add(LibroAux);

        }
    }

    /**
     * Este metodo crea objetos Socio a partir de un ResultSet y los agrega a una lista
     * @param socios Lista donde se almacenan los socios creados
     * @param rs datos de la consulta
     * @throws SQLException Gestion de excepciones
     */
    private static void crearSocio(List<Socio> socios, ResultSet rs) throws SQLException {
        while (rs.next()) {
            int codigo = rs.getInt("codigo");
            String dni = rs.getString("dni");
            String nombre = rs.getString("nombre");
            String domicilio = rs.getString("domicilio");
            String telefono = rs.getString("telefono");
            String correo = rs.getString("correo");

            Socio socioAux = new Socio(codigo, dni, nombre, domicilio, telefono, correo);
            socios.add(socioAux);
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
