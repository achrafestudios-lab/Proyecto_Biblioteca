package socios;

import config.ConfigMySql;
import exception.BDException;
import exception.SociosException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class AccesoSocio {
    public static boolean insertarSocio(Socio socio) {
        Connection conexion = null;
        PreparedStatement ps;

        try{
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
        }catch (SQLException e) {
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

    public static boolean eliminarSocio(int codigo) {
        Connection conexion = null;
        PreparedStatement ps;

        try{
            conexion = ConfigMySql.abrirConexion();

            String query = "DELETE FROM socio WHERE codigo = ?";

            ps = conexion.prepareStatement(query);

            ps.setInt(1, codigo);

            int eliminado = ps.executeUpdate();

            if (eliminado == 0) {
                throw new SociosException(SociosException.SOCIO_INEXISTENTE);
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

        return true;
    }

    public static List<Socio> consultarSocios() throws SociosException {
        List<Socio> socios = new LinkedList<>();
        Connection conexion = null;
        PreparedStatement ps;

        try{
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT * FROM socio";

            ps = conexion.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

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

            if(socios.isEmpty()) {
                throw new SQLException(SociosException.SOCIOS_NO_ENCONTRADOS);
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

    public static List<Socio> consultarSociosPorLocalidadOrdenadoPorNombreAsc(String localidad) throws SociosException {
        List<Socio> socios = new LinkedList<>();
        Connection conexion = null;
        PreparedStatement ps;

        try {
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT * FROM socio WHERE localidad = ? ORDER BY nombre ASC";
            ps=conexion.prepareStatement(query);

            ps.setString(1, localidad);

            ResultSet rs = ps.executeQuery();

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

            if(socios.isEmpty()) {
                throw new SQLException(SociosException.SOCIOS_NO_ENCONTRADOS_LOCALIDAD);
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

    public static List<Socio> consultarSociosSinPrestamos(){
        List<Socio> socios = new LinkedList<>();
        Connection conexion = null;
        PreparedStatement ps;

        try{
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT * FROM socios WHERE codigo NOT IN (SELECT codigo_socio FROM prestamos)";

            ps = conexion.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

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

            if(socios.isEmpty()) {
                throw  new SQLException(SociosException.SOCIOS_NO_ENCONTRADOS_SIN_PRESTAMOS);
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

    public static List<Socio> consultarSociosPorPrestamoEnFecha(String fecha){
        List<Socio> socios = new LinkedList<>();
        Connection conexion = null;
        PreparedStatement ps;

        try{
            conexion = ConfigMySql.abrirConexion();

            String query = "SELECT * FROM socio WHERE codigo IN (SELECT codigo_socio FROM prestamo WHERE fecha_inicio = ?)";
            ps = conexion.prepareStatement(query);
            ps.setString(1, fecha);

            ResultSet rs = ps.executeQuery();

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

            if(socios.isEmpty()) {
                throw new SociosException(SociosException.SOCIOS_NO_ENCONTRADOS_CON_PRESTAMOS);
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

    public static String toStringList(List<Socio> socios) {
        String cadena = "";

        for(Socio socioAux : socios) {
            cadena += socioAux + "\n";
        }
        return cadena;
    }
}
