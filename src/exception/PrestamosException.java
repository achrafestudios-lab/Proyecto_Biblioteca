package exception;

public class PrestamosException extends Exception {

    public static final String ERROR_LIBRO_PRESTADO_A_OTRO_USUARIO = "Se ha prestado ese libro a un socio y éste aún no lo ha devuelto.";
    public static final String ERROR_LIBRO_PRESTADO_A_ESE_USUARIO = "Se ha prestado un libro a ese socio y éste aún no lo ha devuelto.";

    public PrestamosException(String message) {
        super(message);
    }
}
