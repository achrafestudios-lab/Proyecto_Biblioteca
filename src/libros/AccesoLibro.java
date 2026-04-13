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

    /**
     *
     * @param libro Pide un libro para inserter
     * @return Devuelve true si se ha insertado de lo contrast false
     * @throws BDException Gestion de exceptions
     */
    public static boolean insertarLibros(Libro libro) throws BDException {
        Connection conexion = null;
        int columnasInsertadas = 0;

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
     * @param codigo Pide el codigo de un libro
     * @return Devuelve true si se ha eliminado el libro de lo contrast false
     * @throws BDException Gestion de exceptions
     */
    public static boolean eliminarLibro(int codigo) throws BDException {
        PreparedStatement ps;
        Connection conexion = null;

        try {
            conexion = ConfigMySql.abrirConexion();
            String query = "DELETE FROM libro WHERE codigo = ?";
            ps = conexion.prepareStatement(query);
            ps.setInt(1, codigo);

            int resultado = ps.executeUpdate();
            if (resultado == 0) {
                return false;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } finally {
            if (conexion != null) {
                ConfigMySql.cerrarConexion(conexion);
            }
        }

        return true;
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

    public static void main(String[] args) {
        System.out.println(consultarTodosLibros());

        Libro libro = new Libro(5, "123456789", "Prueva libro", "Achraf Diaz", 2000, 9.8);

        if (insertarLibros(libro)) {
            System.out.println("Libro insertado correctamente");
        }

        if (eliminarLibro(5)) {
            System.out.println("Libro eliminado correctamente");
        }

        System.out.println(consultarLibrosPorAutorYPuntuacionDes("Achraf Diaz"));

        System.out.println(consultarLibrosNoPrestados());
    }
}
