package vista;

import entrada.Teclado;

public class LeerValidaciones {

    /**
     * Metodo que pide un ISBN por teclado hasta que sea valido
     * @return ISBN valido introducido por el usuario
     */
    public static String leerISBNValidado() {
        String isbn;
        boolean validado;

        do {
            isbn = Teclado.leerCadena("ISBN: ");
            validado = Validaciones.validarISBN(isbn);

            if (!validado) {
                System.err.println("El ISBN debe comenzar por 978 o 979 y tener 13 digitos.");
            }
        } while (!validado);

        return isbn;
    }

    /**
     * Metodo que pide un año por teclado hasta que sea valido
     * @return Año valido introducido por el usuario
     */
    public static int leerAnioValidado() {
        int anio;
        boolean validado;

        do {
            anio = Teclado.leerEntero("Año: ");
            validado = Validaciones.validarAnio(anio);

            if (!validado) {
                System.err.println("El año no es valido. (YYYY)");
            }
        } while (!validado);

        return anio;
    }

    /**
     * Metodo que pide una puntuacion por teclado hasta que sea valida
     * @return Puntuacion valida introducida por el usuario
     */
    public static double leerPuntuacionValidada() {
        double puntuacion;
        boolean validado;

        do {
            puntuacion = Teclado.leerNatural("Puntuacion: ");
            validado = Validaciones.validarPuntuacion(puntuacion);

            if (!validado) {
                System.err.println("La puntuacion debe estar entre 0 y 10.");
            }
        } while (!validado);

        return puntuacion;
    }

    /**
     * Metodo que pide una fecha por teclado hasta que sea valida
     * @return Fecha valida introducida por el usuario
     */
    public static String leerFechaValidada() {
        String fecha;
        boolean validado;

        do {
            fecha = Teclado.leerCadena("Fecha de devolucion: ");
            validado = Validaciones.validarFecha(fecha);

            if (!validado) {
                System.err.println("Fecha invalida. (YYYY-MM-DD)");
            }
        } while (!validado);

        return fecha;
    }

    /**
     * Metodo que pide un DNI por teclado hasta que sea valido
     * @return DNI valido introducido por el usuario
     */
    public static String leerDNIValidado() {
        String dni;
        boolean validado;

        do {
            dni = Teclado.leerCadena("DNI: ");
            validado = Validaciones.validarDNI(dni);

            if (!validado) {
                System.err.println("DNI invalido. (8 numeros y una letra al final)");
            }
        } while (!validado);

        return dni;
    }

    /**
     * Metodo que pide un nombre por teclado hasta que sea valido
     * @return Nombre valido introducido por el usuario
     */
    public static String leerNombreValidado() {
        String nombre;
        boolean validado;

        do {
            nombre = Teclado.leerCadena("Nombre: ");
            validado = Validaciones.validarNombre(nombre);

            if (!validado) {
                System.err.println("Nombre invalido. (No puede tener numeros)");
            }
        } while (!validado);

        return nombre;
    }

    /**
     * Metodo que pide un telefono por teclado hasta que sea valido
     * @return Telefono valido introducido por el usuario
     */
    public static String leerTelefonoValidado() {
        String telefono;
        boolean validado;

        do {
            telefono = Teclado.leerCadena("Telefono: ");
            validado = Validaciones.validarTelefono(telefono);

            if (!validado) {
                System.err.println("Telefono invalido. (Tiene que tener 9 numeros y no debe contener letras)");
            }
        } while (!validado);

        return telefono;
    }

    /**
     * Metodo que pide un correo por teclado hasta que sea valido
     * @return Correo valido introducido por el usuario
     */
    public static String leerCorreoValidado() {
        String correo;
        boolean validado;

        do {
            correo = Teclado.leerCadena("Correo: ");
            validado = Validaciones.validarCorreo(correo);

            if (!validado) {
                System.err.println("Correo invalido. (No tiene el formato)");
            }
        } while (!validado);

        return correo;
    }
}
