package exception;

public class PrestamosException extends Exception {
    public static final String PRESTAMOS_NO_ENCONTRADOS = "No se ha encontrado ningún préstamo en la base de datos.";
    public static final String PRESTAMOS_NO_DEVUELTOS_INEXISTENTES = "No existe ningún préstamo no devuelto en la base de datos.";
    public static final String PRESTAMOS_INEXISTENTE_POR_FECHA = "No existe ningún préstamo realizado en esa fecha en la base de datos.";
    public static final String ERROR_LIBRO_PRESTADO_A_OTRO_USUARIO = "Se ha prestado ese libro a un socio y éste aún no lo ha devuelto.";
    public static final String ERROR_LIBRO_PRESTADO_A_ESE_USUARIO = "Se ha prestado un libro a ese socio y éste aún no lo ha devuelto.";
    public static final String ERROR_LIBRO_NO_INSERTADO = "No se ha insertado el libro";
    public static final String ERROR_NO_SE_PUDO_ACTUALIZAR_PRESTAMO = "No se ha podido actualizar el prestamo";

    public PrestamosException(String message) {
        super(message);
    }
}
