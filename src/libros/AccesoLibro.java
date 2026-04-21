package libros;

import config.ConfigMySql;
import exception.BDException;
import exception.LibroException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccesoLibro {

    /**
     *
     * @param libro Pide un libro para inserter
     * @return Devuelve true si se ha insertado de lo contrast false
     * @throws BDException Gestion de exceptions
     */
    public static boolean insertarLibros(Libro libro) throws BDException {
        Connection conexion = null;
        int columnasInsertadas;

        try {
            conexion = ConfigMySql.abrirConexion();

            String sentenciaInsertarDept = "INSERT INTO libro(codigo, isbn, titulo, escritor, anio_publicacion, puntuacion) VALUES(?,?,?,?,?,?);";

            PreparedStatement sentencia = conexion.prepareStatement(sentenciaInsertarDept);

            int codigo = libro.getCodigo();
            String isbn = libro.getIsbn();
            String titulo = libro.getTitulo();
            String escritor = libro.getEscritor();
            int anio_publicaccion = libro.getAnioPublicacion();
            double puntuacion = libro.getPuntuacion();

            sentencia.setInt(1, codigo);
            sentencia.setString(2, isbn);
            sentencia.setString(3, titulo);
            sentencia.setString(4, escritor);
            sentencia.setInt(5, anio_publicaccion);
            sentencia.setDouble(6, puntuacion);

            columnasInsertadas = sentencia.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } finally {
            if (conexion != null) {
                ConfigMySql.cerrarConexion(conexion);
            }
        }

        return columnasInsertadas > 0;

    }

    /**
     *
     * @param isbn Pide un ISBN al usuario
     * @throws BDException Gestion de exceptions
     * @throws LibroException Gestion de exceptions
     */
    public static void eliminarLibro(String isbn) throws BDException, LibroException {
        PreparedStatement ps;
        Connection conexion = null;

        try {
            conexion = ConfigMySql.abrirConexion();

            String query1 = "SELECT * FROM prestamo WHERE codigo_libro = (SELECT codigo FROM libro WHERE isbn = ?))";
            ps = conexion.prepareStatement(query1);
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                throw new LibroException(LibroException.ERROR_LIBRO_HA_SIDO_PRESTADO);
            }

            String query2 = "DELETE FROM libro WHERE codigo = (SELECT codigo FROM libro WHERE isbn = ?)";
            ps = conexion.prepareStatement(query2);
            ps.setString(1, isbn);

            int resultado = ps.executeUpdate();
            if (resultado == 0) {
                throw new LibroException(LibroException.ERROR_NO_EXISTE_EL_LIBRO);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } finally {
            if (conexion != null) {
                ConfigMySql.cerrarConexion(conexion);
            }
        }
    }

    /**
     *
     * @return List de todos los libros
     * @throws BDException Gestion de exceptions
     */
    public static List<Libro> consultarTodosLibros() throws BDException {
        List<Libro> LibrosAux = new ArrayList<>();
        PreparedStatement ps;
        Connection conexion = null;

        try {
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT * FROM libro";

            ps = conexion.prepareStatement(query);

            ResultSet resultados = ps.executeQuery();

            crearLibro(LibrosAux, resultados);
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

    /**
     *
     * @param autor Autor para filtrar
     * @return Devuelve todos los libros de un autor ordenados de puntuacion mas peuqeña a grande (des)
     * @throws BDException Gestion de exceptions
     */
    public static List<Libro> consultarLibrosPorAutorYPuntuacionDes(String autor) throws BDException {
        List<Libro> LibrosAux = new ArrayList<>();
        PreparedStatement ps;
        Connection conexion = null;
        try {
            conexion = ConfigMySql.abrirConexion();
            String query = "SELECT * FROM libro WHERE escritor = ? ORDER BY puntuacion DESC";

            ps = conexion.prepareStatement(query);
            ps.setString(1, autor);

            ResultSet resultados = ps.executeQuery();

            crearLibro(LibrosAux, resultados);
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

    /**
     *
     * @return Devuelve una lista con todos los libros no prestados
     * @throws BDException Gestion de exceptions
     */
    public static List<Libro> consultarLibrosNoPrestados() throws BDException {
        List<Libro> LibrosAux = new ArrayList<>();
        PreparedStatement ps;
        Connection conexion = null;
        try {
            conexion = ConfigMySql.abrirConexion();
            String query = "SELECT * FROM libro WHERE codigo NOT IN (SELECT codigo_libro FROM prestamo)";
            ps = conexion.prepareStatement(query);

            ResultSet resultados = ps.executeQuery();

            crearLibro(LibrosAux, resultados);

        } catch (
                SQLException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } catch (
                BDException e) {
            throw new BDException(BDException.ERROR_ABRIR_CONEXION + e.getMessage());
        } finally {
            if (conexion != null) {
                ConfigMySql.cerrarConexion(conexion);
            }
        }

        return LibrosAux;
    }

    /**
     *
     * @param fecha_devolucion Pide una fecha para consultar todos los libros
     * @return Da una lista con todos los libros por fecha
     * @throws BDException Gestion de exceptions
     * @throws LibroException Gestion de exceptions
     */
    public static List<Libro> consultarDevueltosPorFecha(String fecha_devolucion)throws BDException, LibroException{
        List<Libro> LibrosAux = new ArrayList<>();
        PreparedStatement ps;
        Connection conexion = null;
        try {
            conexion = ConfigMySql.abrirConexion();

            String query ="SELECT libro.* FROM libro JOIN prestamo ON (libro.codigo = prestamo.codigo_libro) WHERE prestamo.fecha_devolucion = ?";
            ps = conexion.prepareStatement(query);
            ps.setString(1, fecha_devolucion);
            ResultSet resultados = ps.executeQuery();

            crearLibro(LibrosAux, resultados);

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

    public static void main(String[] args) {
        System.out.println(consultarTodosLibros());

        Libro libro = new Libro(5, "123456789", "Prueva libro", "Achraf Diaz", 2000, 9.8);

        if (insertarLibros(libro)) {
            System.out.println("Libro insertado correctamente");
        }

        System.out.println(consultarLibrosPorAutorYPuntuacionDes("Achraf Diaz"));

        System.out.println(consultarLibrosNoPrestados());
    }
}
