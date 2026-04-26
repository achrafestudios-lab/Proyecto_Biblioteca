package prestamos;

import config.ConfigMySql;
import exception.BDException;
import exception.PrestamosException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.time.LocalDate;

public class AccesoPrestamo {
    /**
     * Este metodo consulta todos los préstamos existentes en la base de datos de la biblioteca
     * @return Devuelve una lista con todos los prestamos
     * @throws PrestamosException Gestion de excepciones de préstamos
     */
    public static List<Prestamo> consultarPrestamos() throws PrestamosException {
        List<Prestamo> prestamos = new LinkedList<>();
        Connection conexion = null;

        try {
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT * FROM prestamo";
            prestamos(prestamos, conexion, query);

            if(prestamos.isEmpty()){
                throw new PrestamosException(PrestamosException.PRESTAMOS_NO_ENCONTRADOS);
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

        return prestamos;
    }

    /**
     * Este metodo consulta todos los prestamos no devueltos en la base de datos de la biblioteca
     * @return Devuelve una lista con los préstamos que no han sido devueltos
     * @throws PrestamosException Gestion de excepciones de préstamos
     */
    public static List<Prestamo> consultarPrestamosNoDevueltos() throws PrestamosException {
        List<Prestamo> prestamos = new LinkedList<>();
        Connection conexion = null;

        try {
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT * FROM prestamo WHERE fecha_devolucion IS NULL";
            prestamos(prestamos, conexion, query);

            if(prestamos.isEmpty()){
                throw new PrestamosException(PrestamosException.PRESTAMOS_NO_DEVUELTOS_INEXISTENTES);
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

        return prestamos;
    }

    public static void prestamos(List<Prestamo> prestamos, Connection conexion, String query) throws SQLException {
        PreparedStatement ps = conexion.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int codigoLibro = rs.getInt("codigo_Libro");
            int codigoSocio = rs.getInt("codigo_socio");
            String fechaInicio = rs.getString("fecha_inicio");
            String fechaFin = rs.getString("fecha_fin");
            String fechaDevolucion = rs.getString("fecha_devolucion");

            Prestamo prestamoAux = new Prestamo(codigoLibro, codigoSocio, fechaInicio, fechaFin, fechaDevolucion);
            prestamos.add(prestamoAux);
        }
    }

    /**
     * Este metodo consulta los préstamos por fecha de inicio en la base de datos de la biblioteca
     * @param fechaInicio Fecha de inicio del prestamo
     * @return Devuelve una lista con el dni, nombre, isbn, título y fecha de devolucion de los préstamos en esa fecha
     * @throws PrestamosException Gestion de excepciones de préstamos
     */
    public static List<ConsultarPrestamosPorFechaInicio> consultarPrestamosPorFechaInicio(String fechaInicio) throws PrestamosException {
        List<ConsultarPrestamosPorFechaInicio> prestamos = new LinkedList<>();
        Connection conexion = null;
        PreparedStatement ps;
        try {
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT s.dni, s.nombre, l.isbn, l.titulo, p.fecha_devolucion" +
                    " FROM prestamo p" +
                    " JOIN libro l ON (codigo_libro = l.codigo)" +
                    " JOIN socio s ON (codigo_socio = s.codigo)" +
                    " WHERE p.fecha_inicio = ?";
            ps=conexion.prepareStatement(query);
            ps.setString(1, fechaInicio);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String dni = rs.getString("dni");
                String nombre = rs.getString("nombre");
                String isbn = rs.getString("isbn");
                String titulo = rs.getString("titulo");
                String fechaDevolucion = rs.getString("fecha_devolucion");
                ConsultarPrestamosPorFechaInicio aux = new ConsultarPrestamosPorFechaInicio(dni, nombre, isbn, titulo, fechaDevolucion);
                prestamos.add(aux);
            }

            if(prestamos.isEmpty()){
                throw new PrestamosException(PrestamosException.PRESTAMOS_INEXISTENTE_POR_FECHA);
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

        return prestamos;
    }

    /**
     * Este metodo inserta un prestamo en la base de datos de la biblioteca
     * @param isbn ISBN del libro a prestar
     * @param dni DNI del socio que realiza el prestamo
     * @throws BDException Gestion de excepciones de base de datos
     * @throws PrestamosException Gestion de excepciones de préstamos
     */
    public static void insertarPrestamo(String isbn, String dni) throws BDException, PrestamosException {
        Connection conexion = null;
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaFin = fechaInicio.plusDays(30);
        String fecha = fechaInicio.getYear() + "-" + fechaInicio.getMonthValue() + "-" + fechaInicio.getDayOfMonth();
        String fechaF = fechaFin.getYear() + "-" + fechaFin.getMonthValue() + "-" + fechaFin.getDayOfMonth();

        try {
            conexion = ConfigMySql.abrirConexion();
            String query = "SELECT s.dni FROM prestamo p" +
                    " JOIN socio s ON (p.codigo_socio = s.codigo)" +
                    " WHERE p.codigo_libro = (SELECT codigo FROM libro WHERE isbn = ?)" +
                    " AND p.fecha_devolucion IS null";
            PreparedStatement sentencia1 = conexion.prepareStatement(query);
            sentencia1.setString(1, isbn);
            ResultSet rs = sentencia1.executeQuery();

            if (rs.next()) {
                String dniConsulta = rs.getString("dni");
                if(!dniConsulta.equalsIgnoreCase(dni)){
                    throw new PrestamosException(PrestamosException.ERROR_LIBRO_PRESTADO_A_OTRO_USUARIO);
                } else {
                    throw new PrestamosException(PrestamosException.ERROR_LIBRO_PRESTADO_A_ESE_USUARIO);
                }
            }

            String sentenciaInsertarDept = "INSERT INTO prestamo (codigo_libro, codigo_socio, fecha_inicio, fecha_fin, fecha_devolucion) " +
                    "VALUES (" +
                    "(SELECT codigo FROM libro WHERE isbn = ?)," +
                    "(SELECT codigo FROM socio WHERE dni = ?)," +
                    "?, ?, NULL)";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaInsertarDept);

            sentencia.setString(1, isbn);
            sentencia.setString(2, dni);
            sentencia.setString(3, fecha);
            sentencia.setString(4, fechaF);

            int insertado = sentencia.executeUpdate();

            if(insertado == 0){
                throw new PrestamosException(PrestamosException.ERROR_LIBRO_NO_INSERTADO);
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
     * Este metodo elimina un prestamo de la base de datos de la biblioteca
     * @param isbn ISBN del libro del prestamo
     * @param dni DNI del socio del prestamo
     * @param fecha_inicio Fecha de inicio del prestamo
     * @return Devuelve true si se ha eliminado correctamente, false si no existe el prestamo
     * @throws BDException Gestion de excepciones de base de datos
     */
    public static boolean eliminarLibro(String isbn, String dni, String fecha_inicio) throws BDException {
        PreparedStatement ps;
        Connection conexion = null;

        try {
            conexion = ConfigMySql.abrirConexion();
            String query = "DELETE FROM prestamo " +
                    "WHERE codigo_libro = (SELECT codigo FROM libro WHERE isbn = ?) " +
                    "AND codigo_socio = (SELECT codigo FROM socio WHERE dni = ?) " +
                    "AND fecha_inicio = ?";
            ps = conexion.prepareStatement(query);
            ps.setString(1, isbn);
            ps.setString(2, dni);
            ps.setString(3, fecha_inicio);

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
     * Este metodo actualiza un prestamo en la base de datos de la biblioteca estableciendo la fecha de devolucion
     * @param isbn ISBN del libro del prestamo
     * @param dni DNI del socio del prestamo
     * @param fecha_inicio Fecha de inicio del prestamo
     * @throws BDException Gestion de excepciones de base de datos
     * @throws PrestamosException Gestion de excepciones de préstamos
     */
    public static void actualizarPrestamo(String isbn, String dni, String fecha_inicio) throws BDException, PrestamosException {
        Connection conexion = null;
        LocalDate fechaInicio = LocalDate.now();
        String fecha_devolucion = fechaInicio.getYear() + "-" + fechaInicio.getMonthValue() + "-" + fechaInicio.getDayOfMonth();


        try {
            conexion = ConfigMySql.abrirConexion();

            String query = "UPDATE prestamo SET fecha_devolucion = ? " +
                    "WHERE codigo_libro = (SELECT codigo FROM libro WHERE isbn = ?) " +
                    "AND codigo_socio = (SELECT codigo FROM socio WHERE dni = ?) " +
                    "AND fecha_inicio = ? ";

            PreparedStatement sentencia = conexion.prepareStatement(query);

            sentencia.setString(1, fecha_devolucion);
            sentencia.setString(2, isbn);
            sentencia.setString(3, dni);
            sentencia.setString(4, fecha_inicio);

            int insertado = sentencia.executeUpdate();

            if(insertado == 0){
                throw new PrestamosException(PrestamosException.ERROR_NO_SE_PUDO_ACTUALIZAR_PRESTAMO);
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

}
