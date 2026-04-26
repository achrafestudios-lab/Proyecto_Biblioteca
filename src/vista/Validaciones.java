package vista;

public class Validaciones {
    public static boolean validarISBN(String isbn){
        return isbn.matches("^97[89][0-9]{10}$");
    }

    public static boolean validarDNI(String dni){
        return dni.matches("^[0-9]{8}[A-Z]$");
    }

    public static boolean validarCorreo(String correo){
        return correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean validarTelefono(String telefono){
        return telefono.matches("^[679][0-9]{8}$");
    }

    public static boolean validarNombre(String nombre){
        return nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ][a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$");
    }

    public static boolean validarFecha(String fecha){
        return fecha.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }

    public static boolean validarAnio(int anio){
        return anio >= 0 && anio <= 9999;
    }

    public static boolean validarPuntuacion(double puntuacion){
        return puntuacion >= 0 && puntuacion <= 10;
    }
}
