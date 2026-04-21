package prestamos;

import config.ConfigMySql;
import exception.BDException;
import exception.PrestamosException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import java.time.LocalDate;

public class AccesoPrestamo {
    /**
     *
     * @return Lista todos los prestamos existentes
     * @throws PrestamosException Por si no hay prestamos en la base de datos
     */
    public static List<Prestamo> consultarPrestamos() throws PrestamosException {
        List<Prestamo> prestamos = new LinkedList<>();
        Connection conexion = null;
        PreparedStatement ps;

        try {
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT * FROM prestamo";
            ps = conexion.prepareStatement(query);
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
     *
     * @return Lista con todos los prestamos no devueltos
     * @throws PrestamosException Por si todos los prestamos estan devueltos
     */
    public static List<Prestamo> consultarPrestamosNoDevueltos() throws PrestamosException {
        List<Prestamo> prestamos = new LinkedList<>();
        Connection conexion = null;
        PreparedStatement ps;

        try {
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT * FROM prestamo WHERE fecha_devolucion IS NULL";
            ps = conexion.prepareStatement(query);
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

    /**
     *
     * @param fechaInicio fecha
     * @return Lista con el dni, nombre, isbn, titulo y fecha de devolucion de un prestamo por fecha
     * @throws PrestamosException Si no hay prestamos en esa fecha
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

            if(rs.isAfterLast()){
                throw new PrestamosException(PrestamosException.PRESTAMOS_INEXISTENTE_POR_FECHA);
            }

            while (rs.next()) {
                String dni = rs.getString("dni");
                String nombre = rs.getString("nombre");
                String isbn = rs.getString("isbn");
                String titulo = rs.getString("titulo");
                String fechaDevolucion = rs.getString("fecha_devolucion");
                ConsultarPrestamosPorFechaInicio aux = new ConsultarPrestamosPorFechaInicio(dni, nombre, isbn, titulo, fechaDevolucion);
                prestamos.add(aux);
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


    public static String toStringList(List<?> lista) {
        String cadena = "";

        for (Object o: lista) {
            cadena += o + "\n";
        }

        return cadena;
    }

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
