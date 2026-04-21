package prestamos;

public class ConsultarPrestamosPorFechaInicio {
    private String dni;
    private String nombre;
    private String isbn;
    private String titulo;
    private String fecha_devolucion;

    /**
     *
     * @param dni
     * @param nombre
     * @param isbn
     * @param titulo
     * @param fecha_devolucion
     */
    public ConsultarPrestamosPorFechaInicio(String dni, String nombre, String isbn, String titulo, String fecha_devolucion) {
        this.dni = dni;
        this.nombre = nombre;
        this.isbn = isbn;
        this.titulo = titulo;
        this.fecha_devolucion = fecha_devolucion;
    }

    /**
     *
     * @return
     */
    public String getDni() {
        return dni;
    }

    /**
     *
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *
     * @return
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     *
     * @return
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     *
     * @return
     */
    public String getFecha_devolucion() {
        return fecha_devolucion;
    }

    @Override
    public String toString() {
        return "ConsultarPrestamosPorFechaInicio{" +
                "dni='" + dni + '\'' +
                ", nombre='" + nombre + '\'' +
                ", isbn='" + isbn + '\'' +
                ", titulo='" + titulo + '\'' +
                ", fecha_devolucion='" + fecha_devolucion + '\'' +
                '}';
    }
}
