package prestamos;

import config.ConfigMySql;
import exception.BDException;
import exception.PrestamosException;
import libros.Libro;
import socios.Socio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class AccesoPrestamo {
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

    public static List<List<String>> consultarPrestamosPorFechaInicio(String fechaInicio) throws PrestamosException {
        List<List<String>> prestamos = new LinkedList<>();
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
                List<String> consultar = new ArrayList<>();
                String dni = rs.getString("dni");
                consultar.add(dni);
                String nombre = rs.getString("nombre");
                consultar.add(nombre);
                String isbn = rs.getString("isbn");
                consultar.add(isbn);
                String titulo = rs.getString("titulo");
                consultar.add(titulo);
                String fechaDevolucion = rs.getString("fecha_devolucion");
                consultar.add(fechaDevolucion);

                prestamos.add(consultar);
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
}
