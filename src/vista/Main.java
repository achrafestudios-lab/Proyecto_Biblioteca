package vista;

import entrada.Teclado;
import exception.BDException;
import exception.SociosException;
import socios.AccesoSocio;
import socios.Socio;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    // ===== MENÚ PRINCIPAL =====
    public static int menuPrincipal() {
        System.out.println("===== MENÚ PRINCIPAL =====");
        System.out.println(" 0. Salir del programa.");
        System.out.println(" 1. Gestión de libros.");
        System.out.println(" 2. Gestión de socios.");
        System.out.println(" 3. Gestión de préstamos.");

        return Teclado.leerEntero("Elige una opción: ");
    }

    // ===== MENÚ LIBROS =====
    public static int menuLibros() {
        System.out.println("\n===== MENÚ LIBROS =====");
        System.out.println(" 0. Volver.");
        System.out.println(" 1. Insertar un libro en la base de datos.");
        System.out.println(" 2. Eliminar un libro, por código, de la base de datos.");
        System.out.println(" 3. Consultar todos los libros de la base de datos.");
        System.out.println(" 4. Consultar varios libros, por escritor, de la base de datos, ordenados por puntuación\n" +
                "decendente.");
        System.out.println(" 5. Consultar los libros no prestados de la base de datos.");
        System.out.println(" 6. Consultar los libros devueltos, en una fecha, de la base de datos.");

        return Teclado.leerEntero("Elige una opción: ");
    }

    // ===== MENÚ SOCIOS =====
    public static int menuSocios() {
        System.out.println("\n===== MENÚ SOCIOS =====");
        System.out.println(" 0. Volver.");
        System.out.println(" 1. Insertar un socio en la base de datos.");
        System.out.println(" 2. Eliminar un socio, por código, de la base de datos.");
        System.out.println(" 3. Consultar todos los socios de la base de datos.");
        System.out.println(" 4. Consultar varios socios, por localidad, de la base de datos, ordenados por nombre\n" +
                "ascendente.");
        System.out.println(" 5. Consultar los socios sin préstamos de la base de datos.");
        System.out.println(" 6. Consultar los socios con préstamos en una fecha de la base de datos.");

        return Teclado.leerEntero("Elige una opción: ");
    }

    // ===== MENÚ PRÉSTAMOS =====
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

    public static void main(String[] args) {
        int opcion;
        List<Socio> socios;
        Socio socio;

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

                            if (opLibro < 0 || opLibro > 6) {
                                System.out.println("La opción de menú debe estar comprendida entre 0 y 6.");
                                continue;
                            }

                            switch (opLibro) {

                                case 1:
                                    System.out.println("Insertando un libro en la base de datos...");
                                    String isbn = Teclado.leerCadena("ISBN: ");
                                    String titulo = Teclado.leerCadena("Título: ");
                                    String escritor = Teclado.leerCadena("Escritor: ");
                                    int anio = Teclado.leerEntero("Año: ");
                                    double puntuacion = Teclado.leerNatural("Puntuación: ");

                                    System.out.println("Se ha insertado un libro en la base de datos.");
                                    break;

                                case 2:
                                    System.out.println("Eliminando un libro, por código, de la base de datos...");
                                    int codigo = Teclado.leerEntero("Código libro: ");

                                    break;

                                case 3:
                                    System.out.println("Consultando todos los libros de la base de datos...");
                                    break;

                                case 4:
                                    System.out.println("Consultando varios libros, por escritor, de la base de datos, ordenados por puntuación decendente...");
                                    String escritorBusqueda = Teclado.leerCadena("Escritor: ");

                                    break;

                                case 5:
                                    System.out.println("Consultando los libros no prestados de la base de datos...");

                                    break;

                                case 6:
                                    System.out.println("Consultando los libros devueltos, en una fecha, de la base de datos...");

                                    String fecha = Teclado.leerCadena("Fecha devolución: ");

                                    break;
                            }

                        } while (opLibro != 0);
                        break;

                    case 2:
                        int opSocio;
                        do {
                            opSocio = menuSocios();

                            if (opSocio < 0 || opSocio > 6) {
                                System.out.println("La opción de menú debe estar comprendida entre 0 y 6.");
                                continue;
                            }

                            switch (opSocio) {
                                case 1:
                                    System.out.println("Insertando un socio en la base de datos...");

                                    String dni = Teclado.leerCadena("DNI: ");
                                    String nombre = Teclado.leerCadena("Nombre: ");
                                    String domicilio = Teclado.leerCadena("Domicilio: ");
                                    String telefono = Teclado.leerCadena("Teléfono: ");
                                    String correo = Teclado.leerCadena("Correo: ");

                                    socio = new Socio(0, dni, nombre, domicilio, telefono, correo);
                                    boolean insertado = AccesoSocio.insertarSocio(socio);

                                    if (insertado) {
                                        System.out.println("Se ha insertado un socio en la base de datos.");
                                    } else {
                                        System.out.println("No se ha insertado el socio en la base de datos.");
                                    }
                                    break;
                                case 2:
                                    System.out.println("Eliminando un socio, por código, de la base de datos....");
                                    int codigoSocio = Teclado.leerEntero("Código socio: ");

                                    AccesoSocio.eliminarSocio(codigoSocio);

                                    System.out.println("Se ha eliminado un socio de la base de datos.");
                                    break;
                                case 3:
                                    System.out.println("Consultando todos los socios de la base de datos...");
                                    socios = AccesoSocio.consultarSocios();

                                    System.out.println(AccesoSocio.toStringList(socios));
                                    System.out.println("Se han encontrado " + socios.size() + " socios en la  base de datos.");
                                    break;

                                case 4:
                                    System.out.println("Consultando varios socios, por localidad, de la base de datos, ordenados por nombre ascendente");
                                    String localidad = Teclado.leerCadena("Localidad: ");

                                    socios = AccesoSocio.consultarSociosPorLocalidadOrdenadoPorNombreAsc(localidad);
                                    System.out.println(AccesoSocio.toStringList(socios));
                                    System.out.println("Se han encontrado " + socios.size() + " socios en la  base de datos.");
                                    break;

                                case 5:
                                    System.out.println("Consultando los socios sin préstamos de la base de datos...");
                                    socios = AccesoSocio.consultarSociosSinPrestamos();

                                    System.out.println(AccesoSocio.toStringList(socios));
                                    System.out.println("Se han encontrado " + socios.size() + " socios en la  base de datos.");
                                    break;
                                case 6:
                                    System.out.println("Consultando los socios con préstamos en una fecha de la base de datos...");
                                    String fechaPrestamo = Teclado.leerCadena("Fecha: ");
                                    socios = AccesoSocio.consultarSociosPorPrestamoEnFecha(fechaPrestamo);

                                    System.out.println(AccesoSocio.toStringList(socios));
                                    System.out.println("Se han encontrado " + socios.size() + " socios en la base de datos.");
                                    break;
                            }

                        } while (opSocio != 0);
                        break;

                    case 3:
                        int opPrestamo;
                        do {
                            opPrestamo = menuSocios();

                            if (opPrestamo < 0 || opPrestamo > 6) {
                                System.out.println("La opción de menú debe estar comprendida entre 0 y 6.");
                                continue;
                            }

                            switch (opPrestamo) {

                                case 1:
                                    System.out.print("Insertando un préstamo en la base de datos...");

                                    break;

                                case 2:
                                    System.out.print("Actualizando un préstamo, por datos identificativos, de la base de datos...");

                                    break;

                                case 3:
                                    System.out.println("Eliminando un préstamo, por datos identificativos, de la base de datos...");

                                    break;

                                case 4:
                                    System.out.println("Consultando todos los préstamos de la base de datos...");

                                    break;

                                case 5:
                                    System.out.println("Consultando los préstamos no devueltos de la base de datos....");
                                    break;

                                case 6:
                                    System.out.print("Consultando DNI y nombre de socio, ISBN y título de libro y fecha de devolución de los préstamos realizados en una fecha de la base de datos...");

                                    break;
                            }

                        } while (opPrestamo != 0);
                        break;

                    default:
                        System.out.println("La opción de menú debe estar comprendida entre 0 y 3.");
                }
            } catch (BDException | SociosException e) {
                System.err.println(e.getMessage());
            }
        } while (opcion != 0);
    }
}