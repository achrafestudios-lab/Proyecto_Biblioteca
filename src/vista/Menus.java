package vista;

import entrada.Teclado;

public class Menus {
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
        System.out.println(" 2. Eliminar un socio, por DNI, de la base de datos.");
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
                "préstamos realizados en la fecha inicio de la base de datos.");

        return Teclado.leerEntero("Elige una opción: ");
    }
}
