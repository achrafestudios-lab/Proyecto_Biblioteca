package vista;

import entrada.Teclado;
import exception.BDException;
import exception.PrestamosException;
import exception.SociosException;
import prestamos.AccesoPrestamo;
import prestamos.ConsultarPrestamosPorFechaInicio;
import prestamos.Prestamo;
import socios.AccesoSocio;
import socios.Socio;
import java.util.List;
import java.util.Map;
import exception.LibroException;
import libros.AccesoLibro;
import libros.Libro;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    /**
     * Menu principal del programa
     * @return Opcion seleccionada por el usuario
     */
    public static int menuPrincipal() {
        System.out.println("===== MENÚ PRINCIPAL =====");
        System.out.println(" 0. Salir del programa.");
        System.out.println(" 1. Gestión de libros.");
        System.out.println(" 2. Gestión de socios.");
        System.out.println(" 3. Gestión de préstamos.");

        return Teclado.leerEntero("Elige una opción: ");
    }

    /**
     * Menu de libros
     * @return Opcion seleccionada por el usuario
     */
    public static int menuLibros() {
        System.out.println("\n===== MENÚ LIBROS =====");
        System.out.println(" 0. Volver.");
        System.out.println(" 1. Insertar un libro en la base de datos.");
        System.out.println(" 2. Eliminar un libro, por ISBN, de la base de datos.");
        System.out.println(" 3. Consultar todos los libros de la base de datos.");
        System.out.println(" 4. Consultar varios libros, por escritor, de la base de datos, ordenados por puntuación decendente.");
        System.out.println(" 5. Consultar los libros no prestados de la base de datos.");
        System.out.println(" 6. Consultar los libros devueltos, en una fecha, de la base de datos.");

        return Teclado.leerEntero("Elige una opción: ");
    }

    /**
     * Menu de socios
     * @return Opcion seleccionada por el usuario
     */
    public static int menuSocios() {
        System.out.println("\n===== MENÚ SOCIOS =====");
        System.out.println(" 0. Volver.");
        System.out.println(" 1. Insertar un socio en la base de datos.");
        System.out.println(" 2. Eliminar un socio, por código, de la base de datos.");
        System.out.println(" 3. Consultar todos los socios de la base de datos.");
        System.out.println(" 4. Consultar varios socios, por localidad, de la base de datos, ordenados por nombre ascendente.");
        System.out.println(" 5. Consultar los socios sin préstamos de la base de datos.");
        System.out.println(" 6. Consultar los socios con préstamos en una fecha de la base de datos.");

        return Teclado.leerEntero("Elige una opción: ");
    }

    /**
     * Menu de prestamos
     * @return Opcion seleccionada por el usuario
     */
    public static int menuPrestamos() {
        System.out.println("\n===== MENÚ PRÉSTAMOS =====");
        System.out.println(" 0. Volver.");
        System.out.println(" 1. Insertar un préstamo en la base de datos.");
        System.out.println(" 2. Actualizar un préstamo, por datos identificativos, de la base de datos.");
        System.out.println(" 3. Eliminar un préstamo, por datos identificativos, de la base de datos.");
        System.out.println(" 4. Consultar todos los préstamos de la base de datos.");
        System.out.println(" 5. Consultar los préstamos no devueltos de la base de datos.");
        System.out.println(" 6. Consultar DNI y nombre de socio, ISBN y título de libro y fecha de devolución de los\n" +
                "préstamos realizados en una fecha de la base de datos.");

        return Teclado.leerEntero("Elige una opción: ");
    }

    /**
     * Este metodo convierte una lista en una cadena de texto
     * @param lista Lista de objetos a convertir
     * @return Devuelve una cadena con todos los elementos de la lista separados por saltos de linea
     */
    public static String toStringList(List<?> lista) {
        String cadena = "";

        for (Object o: lista) {
            cadena += o + "\n";
        }

        return cadena;
    }

    /**
     * Este metodo convierte un mapa en una cadena de texto
     * @param mapa Mapa con los datos a convertir
     * @return Devuelve una cadena con las claves y valores del mapa formateados
     */
    public static String toStringMap(Map<?, Object> mapa) {
        StringBuilder cadena = new StringBuilder();

        for(Map.Entry<?, Object> entry : mapa.entrySet()) {
            cadena.append("Fecha inicio [").append(entry.getKey()).append("]").append(": ").append(entry.getValue()).append("\n");
        }
        return cadena.toString();
    }

    /**
     * Metodo que pide un ISBN por teclado hasta que sea valido
     * @return ISBN valido introducido por el usuario
     */
    private static String leerISBNValidado() {
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
    private static int leerAnioValidado() {
        int anio;
        boolean validado;

        do {
            anio = Teclado.leerEntero("Año: ");
            validado = Validaciones.validarAnio(anio);

            if (!validado) {
                System.err.println("El año no es valido.");
            }
        } while (!validado);

        return anio;
    }

    /**
     * Metodo que pide una puntuacion por teclado hasta que sea valida
     * @return Puntuacion valida introducida por el usuario
     */
    private static double leerPuntuacionValidada() {
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
    private static String leerFechaValidada() {
        String fecha;
        boolean validado;

        do {
            fecha = Teclado.leerCadena("Fecha de devolucion: ");
            validado = Validaciones.validarFecha(fecha);

            if (!validado) {
                System.err.println("Fecha invalida.");
            }
        } while (!validado);

        return fecha;
    }

    /**
     * Metodo que pide un DNI por teclado hasta que sea valido
     * @return DNI valido introducido por el usuario
     */
    private static String leerDNIValidado() {
        String dni;
        boolean validado;

        do {
            dni = Teclado.leerCadena("DNI: ");
            validado = Validaciones.validarDNI(dni);

            if (!validado) {
                System.err.println("DNI invalido.");
            }
        } while (!validado);

        return dni;
    }

    /**
     * Metodo que pide un nombre por teclado hasta que sea valido
     * @return Nombre valido introducido por el usuario
     */
    private static String leerNombreValidado() {
        String nombre;
        boolean validado;

        do {
            nombre = Teclado.leerCadena("Nombre: ");
            validado = Validaciones.validarNombre(nombre);

            if (!validado) {
                System.err.println("Nombre invalido.");
            }
        } while (!validado);

        return nombre;
    }

    /**
     * Metodo que pide un telefono por teclado hasta que sea valido
     * @return Telefono valido introducido por el usuario
     */
    private static String leerTelefonoValidado() {
        String telefono;
        boolean validado;

        do {
            telefono = Teclado.leerCadena("Telefono: ");
            validado = Validaciones.validarTelefono(telefono);

            if (!validado) {
                System.err.println("Telefono invalido.");
            }
        } while (!validado);

        return telefono;
    }

    /**
     * Metodo que pide un correo por teclado hasta que sea valido
     * @return Correo valido introducido por el usuario
     */
    private static String leerCorreoValidado() {
        String correo;
        boolean validado;

        do {
            correo = Teclado.leerCadena("Correo: ");
            validado = Validaciones.validarCorreo(correo);

            if (!validado) {
                System.err.println("Correo invalido.");
            }
        } while (!validado);

        return correo;
    }

    public static void main(String[] args) {
        String dni;
        String nombre;
        String telefono;
        String correo;
        String ciudad;
        String fecha;
        String isbn;
        String titulo;

        int anio;
        double puntuacion;

        List<Socio> socios;
        Map<?, Object> mapa;
        List<Prestamo> prestamos;
        List<Libro> libros;
        Socio socio;
        Libro libro;
        List<ConsultarPrestamosPorFechaInicio> contenido;

        int opcion;

        do {
            opcion = menuPrincipal();

            try {
                switch (opcion) {

                    case 0:
                        System.out.println("Saliendo del programa...");
                        break;

                    case 1:
                        int opLibro;
                        do {
                            opLibro = menuLibros();

                            switch (opLibro) {
                                case 0:
                                    System.out.println("Saliendo del programa...");
                                    break;
                                case 1:
                                    System.out.println("Insertando un libro en la base de datos...");

                                    int codigoNuevo = Teclado.leerEntero("Codigo: ");

                                    isbn = leerISBNValidado();
                                    titulo = Teclado.leerCadena("Titulo: ");
                                    String escritor = Teclado.leerCadena("Escritor: ");
                                    anio = leerAnioValidado();
                                    puntuacion = leerPuntuacionValidada();

                                    libro = new Libro(codigoNuevo, isbn, titulo, escritor, anio, puntuacion);

                                    boolean insertado = AccesoLibro.insertarLibros(libro);

                                    if (insertado) {
                                        System.out.println("Libro insertado correctamente.");
                                    } else {
                                        System.err.println("No se ha podido insertar el libro.");
                                    }
                                    break;
                                case 2:
                                    System.out.println("Eliminando un libro, por código, de la base de datos...");

                                    isbn = leerISBNValidado();

                                    AccesoLibro.eliminarLibro(isbn);
                                    System.out.println("Libro eliminado correctamente.");
                                    break;
                                case 3:
                                    System.out.println("Consultando todos los libros de la base de datos...");

                                    libros = AccesoLibro.consultarTodosLibros();

                                    if (libros.isEmpty()) {
                                        System.err.println("No hay libros en la base de datos.");
                                    } else {
                                        System.out.println(toStringList(libros));
                                    }
                                    break;
                                case 4:
                                    System.out.println("Consultando varios libros, por escritor, de la base de datos, ordenados por puntuación descendente...");
                                    String escritorBusqueda = Teclado.leerCadena("Escritor: ");

                                    libros = AccesoLibro.consultarLibrosPorAutorYPuntuacionDes(escritorBusqueda);

                                    if (libros.isEmpty()) {
                                        System.err.println("No se han encontrado libros de ese escritor.");
                                    } else {
                                        System.out.println(toStringList(libros));
                                    }
                                    break;
                                case 5:
                                    System.out.println("Consultando los libros no prestados de la base de datos...");

                                    libros = AccesoLibro.consultarLibrosNoPrestados();

                                    if (libros.isEmpty()) {
                                        System.err.println("No hay libros disponibles (todos están prestados).");
                                    } else {
                                        System.out.println(toStringList(libros));
                                    }
                                    break;
                                case 6:
                                    System.out.println("Consultando los libros devueltos, en una fecha, de la base de datos...");

                                    fecha = leerFechaValidada();

                                    libros = AccesoLibro.consultarDevueltosPorFecha(fecha);
                                    if (libros.isEmpty()) {
                                        System.err.println("No hay libros devueltos en esa fecha.");
                                    } else {
                                        System.out.println(toStringList(libros));
                                    }
                                    break;
                                default:
                                    System.err.println("La opción de menú debe estar comprendida entre 0 y 6.");
                            }
                        } while (opLibro != 0);
                        break;
                    case 2:
                        int opSocio;
                        do {
                            opSocio = menuSocios();

                            switch (opSocio) {
                                case 0:
                                    System.out.println("Saliendo del programa...");
                                    break;
                                case 1:
                                    System.out.println("Insertando un socio en la base de datos...");

                                    dni = leerDNIValidado();
                                    nombre = leerNombreValidado();
                                    String domicilio = Teclado.leerCadena("Ciudad: ");
                                    telefono = leerTelefonoValidado();
                                    correo = leerCorreoValidado();

                                    socio = new Socio(0, dni, nombre, domicilio, telefono, correo);

                                    boolean insertado = AccesoSocio.insertarSocio(socio);

                                    if (insertado) {
                                        System.out.println("Se ha insertado un socio en la base de datos.");
                                    } else {
                                        System.err.println("No se ha insertado el socio en la base de datos.");
                                    }
                                    break;
                                case 2:
                                    System.out.println("Eliminando un socio, por DNI, de la base de datos....");

                                    dni = leerDNIValidado();

                                    AccesoSocio.eliminarSocio(dni);
                                    System.out.println("Se ha eliminado un socio de la base de datos.");
                                    break;
                                case 3:
                                    System.out.println("Consultando todos los socios de la base de datos...");

                                    socios = AccesoSocio.consultarSocios();

                                    System.out.println(toStringList(socios));
                                    System.out.println("Se han encontrado " + socios.size() + " socios en la  base de datos.");
                                    break;
                                case 4:
                                    System.out.println("Consultando varios socios, por localidad, de la base de datos, ordenados por nombre ascendente");
                                    ciudad = Teclado.leerCadena("Ciudad: ");

                                    socios = AccesoSocio.consultarSociosPorLocalidadOrdenadoPorNombreAsc(ciudad);
                                    System.out.println(toStringList(socios));
                                    System.out.println("Se han encontrado " + socios.size() + " socios en la  base de datos.");
                                    break;

                                case 5:
                                    System.out.println("Consultando los socios sin préstamos de la base de datos...");
                                    socios = AccesoSocio.consultarSociosSinPrestamos();

                                    System.out.println(toStringList(socios));
                                    System.out.println("Se han encontrado " + socios.size() + " socios en la  base de datos.");
                                    break;
                                case 6:
                                    System.out.println("Consultando los socios con préstamos en una fecha de la base de datos...");

                                    fecha = leerFechaValidada();

                                    socios = AccesoSocio.consultarSociosPorPrestamoEnFecha(fecha);

                                    System.out.println(toStringList(socios));
                                    System.out.println("Se han encontrado " + socios.size() + " socios en la base de datos.");
                                    break;
                                default:
                                    System.err.println("La opción de menú debe estar comprendida entre 0 y 6.");
                            }
                        } while (opSocio != 0);
                        break;
                    case 3:
                        int opPrestamo;
                        do {
                            opPrestamo = menuPrestamos();

                            switch (opPrestamo) {
                                case 0:
                                    System.out.println("Saliendo del programa...");
                                    break;
                                case 1:
                                    System.out.println("Insertando un préstamo en la base de datos...");
                                    titulo = Teclado.leerCadena("Titulo: ");

                                    libros = AccesoLibro.consultarPorTituloSinPrestar(titulo);

                                    System.out.println("Libros disponibles para prestar con el titulo: " + titulo);
                                    System.out.println(toStringList(libros));

                                    isbn = leerISBNValidado();
                                    dni = leerDNIValidado();

                                    AccesoPrestamo.insertarPrestamo(isbn, dni);

                                    System.out.println("Prestamo insertado correctamente.");
                                    break;
                                case 2:
                                    System.out.println("Actualizando un préstamo, por datos identificativos, de la base de datos...");

                                    dni = leerDNIValidado();

                                    mapa = AccesoSocio.consultarLibrosPorDNI(dni);

                                    System.out.println("Libros no devueltos del socio con DNI: " + dni);
                                    System.out.println(toStringMap(mapa));

                                    isbn = leerISBNValidado();

                                    String fecha_inicio = Teclado.leerCadena("Fecha inicio de prestamo: ");

                                    AccesoPrestamo.actualizarPrestamo(isbn, dni, fecha_inicio);

                                    System.out.println("Prestamo actualizado con exito");
                                    break;
                                case 3:
                                    System.out.println("Eliminando un préstamo, por datos identificativos, de la base de datos...");

                                    titulo = Teclado.leerCadena("Titulo: ");

                                    libros = AccesoLibro.consultarPorTituloPrestadosYNoDevueltos(titulo);

                                    System.out.println("Libros encontrados con el titulo: " + titulo);
                                    System.out.println(toStringList(libros));

                                    isbn = leerISBNValidado();
                                    dni = leerDNIValidado();

                                    String fecha_ini = Teclado.leerCadena("Fecha inicio del prestamo: ");

                                    boolean eliminado = AccesoPrestamo.eliminarLibro(isbn, dni, fecha_ini);

                                    if (eliminado) {
                                        System.out.println("Prestamo eliminado correctamente.");
                                    } else {
                                        System.err.println("No se ha encontrado el prestamo.");
                                    }

                                    break;
                                case 4:
                                    System.out.println("Consultando todos los préstamos de la base de datos...");
                                    prestamos = AccesoPrestamo.consultarPrestamos();

                                    System.out.println(toStringList(prestamos));
                                    System.out.println("Se han consultado " + prestamos.size() + " prestamos en la base de datos.");
                                    break;
                                case 5:
                                    System.out.println("Consultando los préstamos no devueltos de la base de datos....");
                                    prestamos = AccesoPrestamo.consultarPrestamosNoDevueltos();

                                    System.out.println(toStringList(prestamos));
                                    System.out.println("Se han consultado " + prestamos.size() + " prestamos en la base de datos.");
                                    break;
                                case 6:
                                    System.out.println("Consultando DNI y nombre de socio, ISBN y título de libro y fecha de devolución de los préstamos realizados en una fecha de la base de datos...");

                                    fecha = leerFechaValidada();

                                    contenido = AccesoPrestamo.consultarPrestamosPorFechaInicio(fecha);

                                    System.out.println(toStringList(contenido));
                                    System.out.println("Se han consultado " + contenido.size() + " prestamos en la base de datos.");
                                    break;
                                default:
                                    System.err.println("La opción de menú debe estar comprendida entre 0 y 6.");
                            }
                        } while (opPrestamo != 0);
                        break;
                    default:
                        System.err.println("La opción de menú debe estar comprendida entre 0 y 3.");
                }
            } catch (BDException | SociosException | PrestamosException | LibroException e) {
                System.err.println(e.getMessage());
            }
        } while (opcion != 0);
    }
}
