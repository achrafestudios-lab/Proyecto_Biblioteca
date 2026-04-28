package vista;

import entrada.Teclado;
import exception.*;
import prestamos.AccesoPrestamo;
import prestamos.Ampliacion;
import prestamos.ConsultarPrestamosPorFechaInicio;
import prestamos.Prestamo;
import socios.AccesoSocio;
import socios.Socio;
import java.util.List;
import java.util.Map;

import libros.AccesoLibro;
import libros.Libro;

import static prestamos.Ampliacion.*;
import static vista.LeerValidaciones.*;
import static vista.Menus.*;
import static vista.ToStringCollections.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

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

        List<String> listaStrings;
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
                                    System.out.println("Saliendo del menu libro...");
                                    break;
                                case 1:
                                    System.out.println("Insertando un libro en la base de datos...");

                                    isbn = leerISBNValidado();
                                    titulo = Teclado.leerCadena("Titulo: ");
                                    String escritor = Teclado.leerCadena("Escritor: ");
                                    anio = leerAnioValidado();
                                    puntuacion = leerPuntuacionValidada();

                                    libro = new Libro(0, isbn, titulo, escritor, anio, puntuacion);

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
                                    System.out.println("Saliendo del menu socio...");
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
                                    System.out.println("Saliendo del menu prestamo...");
                                    break;
                                case 1:
                                    System.out.println("Insertando un préstamo en la base de datos...");
                                    titulo = Teclado.leerCadena("Busca el libro a insertar: ");

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

                                    titulo = Teclado.leerCadena("Busca los libros con prestamos devueltos: ");

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

                    case 4:
                        int opAmpliacion;

                        do {
                            opAmpliacion = menuAmpliacion();

                            switch (opAmpliacion) {
                                case 0:
                                    System.out.println("Saliendo del menu ampliacion...");
                                    break;
                                case 1:
                                    System.out.println("Consultar el libro o los libros que ha/n sido prestado/s menos veces (y que como mínimo haya/n sido prestado/s una vez)...");
                                    listaStrings = Ampliacion.consultarLibrosMenosPrestados();
                                    System.out.println(toStringList(listaStrings));
                                    break;
                                case 2:
                                    System.out.println("Consultar el socio o los socios que ha/n realizado más préstamos...");
                                    listaStrings = Ampliacion.consultarSociosConMasPrestamos();
                                    System.out.println(toStringList(listaStrings));
                                    break;
                                case 3:
                                    System.out.println("Consultar los libros que han sido prestados (incluyendo los libros no devueltos) una cantidad de veces inferior a la media...");
                                    listaStrings = Ampliacion.consultarLibrosBajoLaMediaDePrestamos();
                                    System.out.println(toStringList(listaStrings));
                                    break;
                                case 4:
                                    System.out.println("Consultar los socios que han realizado una cantidad de préstamos superior a la media...");
                                    listaStrings = consultarSociosSobreLaMediaDePrestamos();

                                    System.out.println(toStringList(listaStrings));
                                    break;
                                case 5:
                                    System.out.println("Consultar el ISBN, el título y el número de veces de los libros que han sido prestados, ordenados por el número de préstamos descendente....");
                                    listaStrings = consultarRankingLibrosPrestados();

                                    System.out.println(toStringList(listaStrings));
                                    break;
                                case 6:
                                    System.out.println("Consultar el DNI, el nombre y el número de veces de los socios que han realizado préstamos, ordenados por el número de préstamos descendente...");
                                    listaStrings = consultarRankingSociosPorPrestamos();

                                    System.out.println(toStringList(listaStrings));
                                    break;
                                default:
                                    System.err.println("La opción de menú debe estar comprendida entre 0 y 6.");
                            }
                        } while (opAmpliacion != 0);
                        break;

                    default:
                        System.err.println("La opción de menú debe estar comprendida entre 0 y 4.");
                }
            } catch (BDException | SociosException | PrestamosException | LibroException | AmpliacionException e) {
                System.err.println(e.getMessage());
            }
        } while (opcion != 0);
    }
}
