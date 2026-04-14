package exception;

public class PrestamosException extends Exception {
    public static final String PRESTAMOS_NO_ENCONTRADOS = "No se ha encontrado ningún préstamo en la base de datos.";
    public static final String PRESTAMOS_NO_DEVUELTOS_INEXISTENTES = "No existe ningún préstamo no devuelto en la base de datos.";
    public static final String PRESTAMOS_INEXISTENTE_POR_FECHA = "No existe ningún préstamo realizado en esa fecha en la base de datos.";

    public PrestamosException(String message) {
        super(message);
    }
}
