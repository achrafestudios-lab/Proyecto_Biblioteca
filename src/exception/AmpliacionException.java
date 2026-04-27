package exception;

public class AmpliacionException extends Exception {

    public static final String LISTA_SOCIOS_VACIA_EXCEPTION = "No hay socios encontrados";
    public static final String LISTA_LIBROS_VACIA_EXCEPTION = "No hay libros encontrados";


    public AmpliacionException(String message) {
        super(message);
    }
}
