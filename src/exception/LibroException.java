package exception;

public class LibroException extends Exception {

    public static final String ERROR_NO_EXISTE_EL_LIBRO = "No existe ningún libro con ese ISBN en la base de datos. ";
    public static final String ERROR_NO_EXISTE_EL_LIBRO_NOMBRE = "No se encontro ningún libro disponible con ese nombre en la base de datos. ";
    public static final String ERROR_LIBRO_HA_SIDO_PRESTADO = "El libro está referenciado en un préstamo de la base de datos.";

    public LibroException(String message) {
        super(message);
    }
}
