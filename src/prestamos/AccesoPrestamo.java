package prestamos;

import config.ConfigMySql;
import exception.BDException;
import exception.PrestamosException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AccesoPrestamo {

    /**
     *
     * @param isbn
     * @param dni
     * @throws BDException
     * @throws PrestamosException
     */
    public static void insertarPrestamo(String isbn, String dni) throws BDException, PrestamosException {
        Connection conexion = null;
        int columnasInsertadas = 0;
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

            System.out.println(fecha);

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

            columnasInsertadas = sentencia.executeUpdate();

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
     * @param isbn
     * @param dni
     * @param fecha_inicio
     * @return
     * @throws BDException
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
     *
     * @param isbn
     * @param dni
     * @param fecha_inicio
     * @param fecha_devolucion
     * @throws BDException
     */
    public static void actualizarPrestamo(String isbn, String dni, String fecha_inicio, String fecha_devolucion) throws BDException {
        Connection conexion = null;
        int columnasInsertadas = 0;

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

            columnasInsertadas = sentencia.executeUpdate();

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
