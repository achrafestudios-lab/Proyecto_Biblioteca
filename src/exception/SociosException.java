package exception;

public class SociosException extends RuntimeException {
    public static final String SOCIO_INEXISTENTE = "No existe ningún socio con ese código en la base de datos.";
    public static final String SOCIOS_NO_ENCONTRADOS = "No se ha encontrado ningún socio en la base de datos.";
    public static final String SOCIOS_NO_ENCONTRADOS_LOCALIDAD = "No existe ningún socio con esa localidad en la base de datos.";
    public static final String SOCIOS_NO_ENCONTRADOS_SIN_PRESTAMOS = "No existe ningún socio sin préstamos en la base de datos.";
    public static final String SOCIOS_NO_ENCONTRADOS_CON_PRESTAMOS = "No existe ningún socio con préstamos en esa fecha en la base de datos.";

    public SociosException(String message) {
        super(message);
    }
}