package vista;

public class Validaciones {
    public static boolean validarISBN(String isbn){
        return isbn.matches("^97[89][0-9]{6}$");
    }

    public static boolean validarDNI(String dni){
        return dni.matches("^[0-9]{8}[A-Z]$");
    }

    public static boolean validarCorreo(String correo){
        return correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }
}
