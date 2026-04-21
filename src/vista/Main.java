package vista;

import entrada.Teclado;
import exception.BDException;
import exception.PrestamosException;
import exception.SociosException;
import prestamos.AccesoPrestamo;
import prestamos.Prestamo;
import socios.AccesoSocio;
import socios.Socio;

import java.util.List;

import exception.LibroException;
import libros.AccesoLibro;
import libros.Libro;

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
        String dni;
        String nombre;
        String telefono;
        String correo;
        String ciudad;
        String fechaPrestamo;
        boolean validado;

        List<Socio> socios;
        List<Prestamo> prestamos;
        Socio socio;
        Prestamo prestamo;
        List<List<String>> contenido;

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

                            if (opLibro < 0 || opLibro > 6) {
                                System.out.println("La opción de menú debe estar comprendida entre 0 y 6.");
                                continue;
                            }

                            switch (opLibro) {

                                case 1:
                                    System.out.println("Insertando un libro en la base de datos...");
                                    int codigoNuevo = Teclado.leerEntero("Codigo: ");
                                    String isbn = Teclado.leerCadena("ISBN: ");
                                    String titulo = Teclado.leerCadena("Titulo: ");
                                    String escritor = Teclado.leerCadena("Escritor: ");
                                    int anio = Teclado.leerEntero("Año: ");
                                    double puntuacion = Teclado.leerNatural("Puntuación: ");

                                    Libro libroNuevo = new Libro(codigoNuevo, isbn, titulo, escritor, anio, puntuacion);
                                    boolean insertado = AccesoLibro.insertarLibros(libroNuevo);
                                    if (insertado) {
                                        System.out.println("Libro insertado correctamente.");
                                    } else {
                                        System.out.println("No se ha podido insertar el libro.");
                                    }
                                    break;

                                case 2:
                                    System.out.println("Eliminando un libro, por código, de la base de datos...");
                                    isbn = Teclado.leerCadena("ISBN: ");
                                    AccesoLibro.eliminarLibro(isbn);
                                    System.out.println("Libro eliminado correctamente.");
                                    break;

                                case 3:
                                    System.out.println("Consultando todos los libros de la base de datos...");

                                    List<Libro> todosLibros = AccesoLibro.consultarTodosLibros();
                                    if (todosLibros.isEmpty()) {
                                        System.out.println("No hay libros en la base de datos.");
                                    } else {
                                        for (Libro l : todosLibros) {
                                            System.out.println(l);
                                        }
                                    }
                                    break;

                                case 4:
                                    System.out.println("Consultando varios libros, por escritor, de la base de datos, ordenados por puntuación descendente...");
                                    String escritorBusqueda = Teclado.leerCadena("Escritor: ");

                                    List<Libro> librosPorAutor = AccesoLibro.consultarLibrosPorAutorYPuntuacionDes(escritorBusqueda);
                                    if (librosPorAutor.isEmpty()) {
                                        System.out.println("No se han encontrado libros de ese escritor.");
                                    } else {
                                        for (Libro l : librosPorAutor) {
                                            System.out.println(l);
                                        }
                                    }
                                    break;

                                case 5:
                                    System.out.println("Consultando los libros no prestados de la base de datos...");
                                    List<Libro> librosNoPrestados = AccesoLibro.consultarLibrosNoPrestados();
                                    if (librosNoPrestados.isEmpty()) {
                                        System.out.println("No hay libros disponibles (todos están prestados).");
                                    } else {
                                        for (Libro l : librosNoPrestados) {
                                            System.out.println(l);
                                        }
                                    }
                                    break;

                                case 6:
                                    System.out.println("Consultando los libros devueltos, en una fecha, de la base de datos...");
                                    String fecha = Teclado.leerCadena("Fecha devolución (YYYY-MM-DD): ");

                                    List<Libro> librosDevueltos = AccesoLibro.consultarDevueltosPorFecha(fecha);
                                    if (librosDevueltos.isEmpty()) {
                                        System.out.println("No hay libros devueltos en esa fecha.");
                                    } else {
                                        for (Libro l : librosDevueltos) {
                                            System.out.println(l);
                                        }
                                    }

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

                                    do{
                                        dni = Teclado.leerCadena("DNI: ");
                                        validado = Validaciones.validarDNI(dni);

                                        if(!validado){
                                            System.out.println("DNI invalido.");
                                        }
                                    }while (!validado);

                                    do{
                                        nombre = Teclado.leerCadena("Nombre: ");
                                        validado = Validaciones.validarNombre(nombre);

                                        if(!validado){
                                            System.out.println("Nombre invalido.");
                                        }
                                    }while(!validado);

                                    String domicilio = Teclado.leerCadena("Ciudad: ");

                                    do{
                                        telefono = Teclado.leerCadena("Teléfono: ");
                                        validado = Validaciones.validarTelefono(telefono);

                                        if(!validado){
                                            System.out.println("Telefono invalido.");
                                        }
                                    }while(!validado);

                                    do{
                                        correo = Teclado.leerCadena("Correo: ");
                                        validado = Validaciones.validarCorreo(correo);

                                        if(!validado){
                                            System.out.println("Correo invalido.");
                                        }
                                    }while (!validado);

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
                                    do{
                                        dni = Teclado.leerCadena("DNI: ");
                                        validado = Validaciones.validarDNI(dni);

                                        if(!validado){
                                            System.out.println("DNI invalido.");
                                        }
                                    }while (!validado);

                                    AccesoSocio.eliminarSocio(dni);
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
                                    ciudad = Teclado.leerCadena("Ciudad: ");

                                    socios = AccesoSocio.consultarSociosPorLocalidadOrdenadoPorNombreAsc(ciudad);
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
                                    do{
                                        fechaPrestamo = Teclado.leerCadena("Fecha: ");
                                        validado = Validaciones.validarFecha(fechaPrestamo);

                                        if(!validado){
                                            System.out.println("Fecha invalida.");
                                        }
                                    }while (!validado);

                                    socios = AccesoSocio.consultarSociosPorPrestamoEnFecha(fechaPrestamo);

                                    System.out.println(AccesoSocio.toStringList(socios));
                                    System.out.println("Se han encontrado " + socios.size() + " socios en la base de datos.");
                                    break;
                                default:
                                    System.out.println("La opción de menú debe estar comprendida entre 0 y 6.");
                            }

                        } while (opSocio != 0);
                        break;

                    case 3:
                        int opPrestamo;
                        do {
                            opPrestamo = menuPrestamos();

                            if (opPrestamo < 0 || opPrestamo > 6) {
                                System.out.println("La opción de menú debe estar comprendida entre 0 y 6.");
                                continue;
                            }

                            switch (opPrestamo) {

                                case 1:
                                    System.out.print("Insertando un préstamo en la base de datos...");
                                    String isbn = Teclado.leerCadena("ISBN: ");
                                    dni = Teclado.leerCadena("DNI: ");

                                    AccesoPrestamo.insertarPrestamo(isbn, dni);
                                    System.out.println("Préstamo insertado correctamente.");
                                    break;

                                case 2:
                                    System.out.println("Actualizando un préstamo, por datos identificativos, de la base de datos...");

                                    isbn = Teclado.leerCadena("Codigo ISBN libro para dar de baja");
                                    dni = Teclado.leerCadena("DNI socio para dar de baja");
                                    String fecha_inicio = Teclado.leerCadena("Fecha inicio de prestamo: ");
                                    String fecha_baja = Teclado.leerCadena("Fecha baja de prestamo: ");

                                    AccesoPrestamo.actualizarPrestamo(isbn, dni, fecha_inicio, fecha_baja);
                                    System.out.println("Libro dado de baja con exito");

                                    break;

                                case 3:
                                    System.out.println("Eliminando un préstamo, por datos identificativos, de la base de datos...");

                                    isbn = Teclado.leerCadena("Codigo ISBN libro para dar de baja");
                                    dni = Teclado.leerCadena("DNI socio para dar de baja");
                                    String fecha_ini = Teclado.leerCadena("Fecha inicio del préstamo: ");

                                    boolean eliminado = AccesoPrestamo.eliminarLibro(isbn, dni, fecha_ini);
                                    if (eliminado) {
                                        System.out.println("Préstamo eliminado correctamente.");
                                    } else {
                                        System.out.println("No se ha encontrado el préstamo.");
                                    }
                                    break;

                                case 4:
                                    System.out.println("Consultando todos los préstamos de la base de datos...");
                                    prestamos = AccesoPrestamo.consultarPrestamos();

                                    System.out.println(AccesoPrestamo.toStringList(prestamos));
                                    System.out.println("Se han consultado " + prestamos.size() + " prestamos en la base de datos.");
                                    break;

                                case 5:
                                    System.out.println("Consultando los préstamos no devueltos de la base de datos....");
                                    prestamos = AccesoPrestamo.consultarPrestamosNoDevueltos();

                                    System.out.println(AccesoPrestamo.toStringList(prestamos));
                                    System.out.println("Se han consultado " + prestamos.size() + " prestamos en la base de datos.");
                                    break;
                                case 6:
                                    System.out.println("Consultando DNI y nombre de socio, ISBN y título de libro y fecha de devolución de los préstamos realizados en una fecha de la base de datos...");
                                    do{
                                        fechaPrestamo = Teclado.leerCadena("Fecha prestamo: ");
                                        validado = Validaciones.validarFecha(fechaPrestamo);

                                        if(!validado){
                                            System.out.println("Fecha inicio de prestamo no valido");
                                        }
                                    }while (!validado);
                                    contenido = AccesoPrestamo.consultarPrestamosPorFechaInicio(fechaPrestamo);

                                    System.out.println(AccesoPrestamo.toStringList(contenido));
                                    System.out.println("Se han consultado " + contenido.size() + " prestamos en la base de datos.");
                                    break;
                                default:
                                    System.out.println("La opción de menú debe estar comprendida entre 0 y 6.");
                            }

                        } while (opPrestamo != 0);
                        break;

                    default:
                        System.out.println("La opción de menú debe estar comprendida entre 0 y 3.");
                }
            } catch (BDException | SociosException | PrestamosException | LibroException e) {
                System.err.println(e.getMessage());
            }

        } while (opcion != 0);
    }
}