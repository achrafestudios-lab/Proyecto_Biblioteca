package vista;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validaciones {
    public static boolean validarISBN(String isbn){
        return isbn.matches("^97[89][0-9]{10}$");
    }

    public static boolean validarDNI(String dni) {

        if (dni == null || !dni.matches("^[0-9]{8}[A-Z]$")) {
            return false;
        }

        int numero = Integer.parseInt(dni.substring(0, 8));
        char letraProporcionada = dni.charAt(8);

        String letrasValidas = "TRWAGMYFPDXBNJZSQVHLCKE";
        char letraCorrecta = letrasValidas.charAt(numero % 23);

        return letraProporcionada == letraCorrecta;
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
        try {
            LocalDate fechaInput = LocalDate.parse(fecha); // Valida formato y lógica
            LocalDate hoy = LocalDate.now(); // Obtiene la fecha actual

            if (fechaInput.isAfter(hoy)) {
                return false;
            }

            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean validarAnio(int anio){
        LocalDate fecha = LocalDate.now();
        return anio > 0 && anio <= fecha.getYear();
    }

    public static boolean validarPuntuacion(double puntuacion){
        return puntuacion >= 0 && puntuacion <= 10;
    }
}
