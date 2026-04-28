package socios;

import comparator.OrdenarPorCodigoLibroComparator;
import config.ConfigMySql;
import exception.BDException;
import exception.PrestamosException;
import exception.SociosException;
import libros.Libro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AccesoSocio {
    /**
     * Este metodo inserta un socio en la base de datos de la biblioteca
     * @param socio Socio a insertar
     * @return Devuelve true si se ha insertado correctamente false en caso contrario
     */
    public static boolean insertarSocio(Socio socio) throws BDException{
        Connection conexion = null;
        PreparedStatement ps;

        try {
            conexion = ConfigMySql.abrirConexion();

            String query = "INSERT INTO socio (dni, nombre, domicilio, telefono, correo) VALUES (?,?,?,?,?)";
            ps = conexion.prepareStatement(query);

            ps.setString(1, socio.getDni());
            ps.setString(2, socio.getNombre());
            ps.setString(3, socio.getDomicilio());
            ps.setString(4, socio.getTelefono());
            ps.setString(5, socio.getCorreo());

            int insertado = ps.executeUpdate();

            if (insertado == 0) {
                return false;
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

        return true;
    }

    /**
     * Este metodo elimina un socio de la base de datos de la biblioteca
     * @param dni DNI del socio a eliminar
     * @throws SociosException Gestion de excepciones de socios
     */
    public static void eliminarSocio(String dni) throws SociosException {
        Connection conexion = null;
        PreparedStatement ps;

        try {
            conexion = ConfigMySql.abrirConexion();

            String query1 = "SELECT * FROM prestamo WHERE codigo_socio = (SELECT codigo FROM socio WHERE dni = ?) AND fecha_devolucion IS NULL";
            ps = conexion.prepareStatement(query1);
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                throw new SociosException(SociosException.SOCIOS_REFERENCIADO_EN_PRESTAMO);
            }

            String query2 = "DELETE FROM socio WHERE dni = ?";
            ps = conexion.prepareStatement(query2);
            ps.setString(1, dni);

            int eliminado = ps.executeUpdate();

            if (eliminado == 0) {
                throw new SociosException(SociosException.SOCIO_INEXISTENTE);
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
    }

    /**
     * Este metodo consulta todos los socios de la base de datos de la biblioteca
     * @return Devuelve una lista con todos los socios
     * @throws SociosException Gestion de excepciones de socios
     */
    public static List<Socio> consultarSocios() throws SociosException {
        List<Socio> socios = new LinkedList<>();
        Connection conexion = null;
        PreparedStatement ps;

        try {
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT * FROM socio";

            ps = conexion.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            crearSocio(socios, rs);

            if (socios.isEmpty()) {
                throw new SociosException(SociosException.SOCIOS_NO_ENCONTRADOS);
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

        return socios;
    }

    /**
     * Este metodo consulta los socios por localidad ordenados por nombre de forma ascendente en la base de datos de la biblioteca
     * @param localidad Localidad a filtrar
     * @return Devuelve una lista de socios que coinciden con la localidad ordenados por nombre ascendente
     * @throws SociosException Gestion de excepciones de socios
     */
    public static List<Socio> consultarSociosPorLocalidadOrdenadoPorNombreAsc(String localidad) throws SociosException {
        List<Socio> socios = new LinkedList<>();
        Connection conexion = null;
        PreparedStatement ps;
        localidad = localidad.toLowerCase();

        try {
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT * FROM socio WHERE LOWER(domicilio) like ? ORDER BY nombre ASC";
            ps = conexion.prepareStatement(query);

            ps.setString(1, "%" + localidad + "%");

            ResultSet rs = ps.executeQuery();

            crearSocio(socios, rs);

            if (socios.isEmpty()) {
                throw new SociosException(SociosException.SOCIOS_NO_ENCONTRADOS_LOCALIDAD);
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

        return socios;
    }

    /**
     * Este metodo consulta los libros prestados a un socio por su DNI en la base de datos de la biblioteca
     * @param dni DNI del socio a consultar
     * @return Devuelve un mapa con los libros prestados y su fecha de inicio
     * @throws PrestamosException Gestion de excepciones de prestamos
     */
    public static Map<Libro, Object> consultarLibrosPorDNI(String dni) throws PrestamosException {
        Map<Libro, Object> libros = new TreeMap<>(new OrdenarPorCodigoLibroComparator());
        Connection conexion = null;
        PreparedStatement ps;
        dni = dni.toUpperCase();

        try {
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT l.*, p.fecha_inicio FROM libro l" +
                    " JOIN prestamo p ON (l.codigo = p.codigo_libro)" +
                    " JOIN socio s ON (p.codigo_socio = s.codigo)" +
                    " WHERE p.fecha_devolucion IS NULL" +
                    " AND s.dni = ?";
            ps = conexion.prepareStatement(query);
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int codigo = rs.getInt("codigo");
                String isbn = rs.getString("isbn");
                String titulo = rs.getString("titulo");
                String escritor = rs.getString("escritor");
                int anio_publicaccion = rs.getInt("anio_publicacion");
                double puntuacion = rs.getDouble("puntuacion");
                String fechaInicio = rs.getString("fecha_inicio");

                Libro LibroAux = new Libro(codigo, isbn, titulo, escritor, anio_publicaccion, puntuacion);

                libros.put(LibroAux, fechaInicio);
            }

            if (libros.isEmpty()) {
                throw new PrestamosException(PrestamosException.PRESTAMOS_NO_DEVUELTOS_INEXISTENTES);
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

        return libros;
    }

    /**
     * Este metodo consulta los socios que no tienen prestamos en la base de datos de la biblioteca
     * @return Devuelve una lista con los socios sin prestamos
     */
    public static List<Socio> consultarSociosSinPrestamos() throws SociosException {
        List<Socio> socios = new LinkedList<>();
        Connection conexion = null;
        PreparedStatement ps;

        try {
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT * FROM socio WHERE codigo NOT IN (SELECT codigo_socio FROM prestamo WHERE fecha_devolucion IS NULL)";

            ps = conexion.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            crearSocio(socios, rs);

            if (socios.isEmpty()) {
                throw new SociosException(SociosException.SOCIOS_NO_ENCONTRADOS_SIN_PRESTAMOS);
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

        return socios;
    }

    /**
     * Este metodo consulta los socios que han realizado un prestamo en una fecha concreta en la base de datos de la biblioteca
     * @param fecha Fecha de inicio del prestamo
     * @return Devuelve una lista con los socios que tienen prestamos en esa fecha
     * @throws SociosException Gestion de excepciones de socios
     */
    public static List<Socio> consultarSociosPorPrestamoEnFecha(String fecha) throws SociosException {
        List<Socio> socios = new LinkedList<>();
        Connection conexion = null;
        PreparedStatement ps;

        try {
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT * FROM socio WHERE codigo IN (SELECT codigo_socio FROM prestamo WHERE fecha_inicio = ?)";
            ps = conexion.prepareStatement(query);
            ps.setString(1, fecha);

            ResultSet rs = ps.executeQuery();

            crearSocio(socios, rs);

            if (socios.isEmpty()) {
                throw new SociosException(SociosException.SOCIOS_NO_ENCONTRADOS_CON_PRESTAMOS);
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

        return socios;
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
    }

}
