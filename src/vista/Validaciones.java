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

    public static boolean validarTelefono(String telefono){
        return telefono.matches("^6[0-9]{8}$");
    }

    public static boolean validarNombre(String nombre){
        return nombre.matches("^[a-zA-Z]\\w+$");
    }

    public static boolean validarFecha(String fecha){
        return fecha.matches("^\\d{2}-\\d{2}-\\d{2}$");
    }
}
